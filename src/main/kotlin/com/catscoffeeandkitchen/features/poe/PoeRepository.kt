package com.catscoffeeandkitchen.features.poe

import com.catscoffeeandkitchen.features.common.toReturnableHttpException
import com.catscoffeeandkitchen.features.poe.models.CurrencyEquivalent
import com.catscoffeeandkitchen.features.poe.models.ItemPrice
import com.catscoffeeandkitchen.features.poe.models.PriceResponse
import com.catscoffeeandkitchen.features.poe.models.ninja.CurrencyDetails
import com.catscoffeeandkitchen.features.poe.models.ninja.CurrencyOverview
import com.catscoffeeandkitchen.features.poe.models.poeprices.PriceInfoResult
import com.catscoffeeandkitchen.features.poe.models.trade.TradeItemID
import com.catscoffeeandkitchen.features.poe.models.trade.bulk.BulkTradeRequest
import com.catscoffeeandkitchen.features.poe.models.trade.bulk.BulkTradeResponse
import com.catscoffeeandkitchen.features.poe.models.trade.search.SearchIdsResponse
import com.catscoffeeandkitchen.features.poe.models.trade.search.SearchTradeRequest
import com.catscoffeeandkitchen.features.poe.models.trade.search.SearchTradeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.util.encodeBase64
import kotlinx.serialization.json.Json
import me.xdrop.fuzzywuzzy.FuzzySearch

class PoeRepository(
    private val httpClient: HttpClient,
    private val baseData: PoeBaseData
) {
    private val lenientJson = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    private fun createCurrencyEquivalent(
        name: String,
        overview: CurrencyOverview,
        currencies: List<CurrencyDetails>
    ): CurrencyEquivalent? {
        val line = overview.lines.find { it.currencyTypeName == name }
        val usePay = line?.pay?.value != null && line.pay.value > 1
        val paying = currencies.find { details ->
            if (usePay) details.id == line?.pay?.payCurrencyId
            else details.id == line?.receive?.payCurrencyId
        }
        val receiving = currencies.find { details ->
            if (usePay) details.id == line?.pay?.getCurrencyId
            else details.id == line?.receive?.getCurrencyId
        }

        return if (paying == null || receiving == null) null
        else {
            CurrencyEquivalent(
                paying = ItemPrice(
                    name = if (usePay) receiving.name else paying.name,
                    icon = if (usePay) receiving.icon else paying.icon,
                    amount = (if (usePay) 1f else line?.receive?.value?.toFloat()) ?: 1f
                ),
                receiving = ItemPrice(
                    name = if (usePay) paying.name else receiving.name,
                    icon = if (usePay) paying.icon else receiving.icon,
                    amount = (if (usePay) line?.pay?.value?.toFloat() else 1f) ?: 1f
                )
            )
        }
    }

    suspend fun getCurrencyInChaos(search: String): List<CurrencyEquivalent> {
        try {
            val currencyOverview = baseData.getCurrencyInfo()
            val currencies = currencyOverview.currencyDetails

            val adjustedSearch = if (search.trim().equals("gcp", ignoreCase = true)) {
                "gemcutter's prism"
            } else if (search.trim().equals("alt", ignoreCase = true)) {
                "alteration"
            } else if (search.trim().equals("div", ignoreCase = true)) {
                "divine"
            } else {
                search
            }
            val results = FuzzySearch.extractSorted(adjustedSearch, currencies.map { it.name })
                .filter { it.score > SEARCH_SCORE_THRESHOLD }

            return results.mapNotNull { currencyName ->
                createCurrencyEquivalent(
                    name = currencyName.string,
                    overview = currencyOverview,
                    currencies = currencies
                )
            }
        } catch (error: ClientRequestException) {
            throw error.toReturnableHttpException()
        }
    }

    private fun BulkTradeResponse.Offer.toDetailString(): String {
        return "${item.amount} ${item.currency} = ${exchange.amount} ${exchange.currency}"
    }

    private suspend fun getBulkPricing(itemId: String, tradeData: List<TradeItemID>? = null): CurrencyEquivalent? {
        val result = httpClient.post {
            url("https://www.pathofexile.com/api/trade/exchange/${baseData.getCurrentLeagueName()}")
            contentType(ContentType.Application.Json)
            setBody(BulkTradeRequest.forItem(itemId))
        }

        val tradeResponse = result.body<BulkTradeResponse>()
        val tradeOffers = tradeResponse.result.entries
            .filter { it.value.listing.offers.isNotEmpty() }
            .take(OFFERS_TO_USE)

        if (tradeOffers.isEmpty()) return null

        val mostCommon = tradeOffers
            .groupingBy { it.value.listing.offers.first().toDetailString() }
            .eachCount()
            .maxByOrNull { it.value }?.key

        val commonEntry = tradeOffers.first { entry ->
            entry.value.listing.offers.first().toDetailString() == mostCommon
        }
        val offer = commonEntry.value.listing.offers.first()

        val tradeSite = "https://www.pathofexile.com"
        return offer.toCurrencyEquivalent(
            payingIcon = tradeData?.find { it.text.contains(offer.exchange.currency, ignoreCase = true) }?.image
                ?.let { "$tradeSite$it" },
            receivingIcon = tradeData?.find { it.text.contains(offer.item.currency, ignoreCase = true) }?.image
                ?.let { "$tradeSite$it" },
        ).copy(
            tradeUrl = "$tradeSite/trade/exchange/${baseData.getCurrentLeagueName()}/${tradeResponse.id}"
        )
    }

    suspend fun searchForBulkPricing(search: String): CurrencyEquivalent? {
        try {
            val tradeData = baseData.getTradeData()
            val result = FuzzySearch.extractOne(search, tradeData.map { it.text })

            return getBulkPricing(tradeData[result.index].id, tradeData)
        } catch (error: ClientRequestException) {
            throw error.toReturnableHttpException()
        }
    }

    suspend fun searchForPricing(search: String): CurrencyEquivalent {
        try {
            val initial = httpClient.post {
                url("https://www.pathofexile.com/api/trade/search/${baseData.getCurrentLeagueName()}")
                contentType(ContentType.Application.Json)
                setBody(SearchTradeRequest.forSearch(search))
            }
            val tradeResponse = initial.body<SearchIdsResponse>()
            val tradeIds = tradeResponse.result.take(MAX_FETCH_IDS).joinToString(",")
            val response = httpClient.get("https://www.pathofexile.com/api/trade/fetch/$tradeIds")

            val listings = response.body<SearchTradeResponse>().result
            val mostCommon = listings
                .groupingBy { it.listing.price.amount }
                .eachCount()
                .maxByOrNull { it.value }?.key

            val commonEntry = listings.first { it.listing.price.amount == mostCommon }
            return CurrencyEquivalent(
                paying = ItemPrice(
                    name = commonEntry.listing.price.currency,
                    amount = commonEntry.listing.price.amount.toFloat(),
                    icon = null
                ),
                receiving = ItemPrice(
                    name = commonEntry.item?.name.takeUnless { it.orEmpty().isEmpty() }
                        ?: commonEntry.item?.typeLine
                        ?: commonEntry.item?.baseType.orEmpty(),
                    amount = commonEntry.item?.stackSize?.toFloat() ?: 1f,
                    icon = commonEntry.item?.icon
                )
            ).copy(
                tradeUrl = "https://pathofexile.com/trade/search" +
                        "/${baseData.getCurrentLeagueName()}/${tradeResponse.id}"
            )
        } catch (error: ClientRequestException) {
            throw error.toReturnableHttpException()
        }
    }

    suspend fun searchItemPrice(item: String): PriceResponse {
        try {
            val leagueName = baseData.getCurrentLeagueName()
            val itemName = item.substringBefore("----")
                .split("\n")
                .filterNot { it.isBlank() }
                .last()
            println("Checking price for $itemName")
            println("input = $item")

            val response = httpClient.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "www.poeprices.info"
                    path("api")

                    parameters.append("i", item.encodeBase64())
                    parameters.append("l", leagueName)
                }
            }

            val channel = response.bodyAsChannel()
            channel.awaitContent()
            val read = channel.readUTF8Line(response.contentLength()?.toInt() ?: 0)

            val price = lenientJson.decodeFromString<PriceInfoResult>(read.orEmpty())
            return PriceResponse(
                name = itemName,
                min = price.min,
                max = price.max,
                currency = price.currency,
                confidence = price.confidence.toFloat(),
                warning = price.warningMessage.takeIf { it.isNotEmpty() },
                error = price.errorMessage.takeIf { it.isNotEmpty() }
            )
        } catch (error: ClientRequestException) {
            throw error.toReturnableHttpException()
        }
    }

    companion object {
        const val SEARCH_SCORE_THRESHOLD = 70
        const val OFFERS_TO_USE = 10
        const val MAX_FETCH_IDS = 10
    }
}
