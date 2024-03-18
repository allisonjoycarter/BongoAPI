package com.catscoffeeandkitchen.features.poe

import com.catscoffeeandkitchen.features.common.ReturnableHttpException
import com.catscoffeeandkitchen.features.common.toReturnableHttpException
import com.catscoffeeandkitchen.features.poe.models.CurrencyEquivalent
import com.catscoffeeandkitchen.features.poe.models.ItemPrice
import com.catscoffeeandkitchen.features.poe.models.PriceResponse
import com.catscoffeeandkitchen.features.poe.models.ninja.CurrencyOverview
import com.catscoffeeandkitchen.features.poe.models.ninja.IndexState
import com.catscoffeeandkitchen.features.poe.models.poeprices.PriceInfoResult
import com.catscoffeeandkitchen.features.poe.models.trade.bulk.BulkTradeRequest
import com.catscoffeeandkitchen.features.poe.models.trade.bulk.BulkTradeResponse
import com.catscoffeeandkitchen.features.poe.models.trade.search.SearchTradeRequest
import com.catscoffeeandkitchen.features.poe.models.trade.StaticTradeResponse
import com.catscoffeeandkitchen.features.poe.models.trade.TradeItemID
import com.catscoffeeandkitchen.features.poe.models.trade.search.SearchTradeResponse
import com.catscoffeeandkitchen.features.poe.models.trade.search.SearchIdsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.util.decodeString
import io.ktor.util.encodeBase64
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.nio.ByteBuffer
import java.nio.charset.Charset

class PoeRepository(private val httpClient: HttpClient) {
    private val lenientJson = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    private suspend fun getLeagues(): List<String> {
        val response = httpClient.get("https://poe.ninja/api/data/getindexstate")
        return response.body<IndexState>().economyLeagues.map { it.displayName }
    }

    private suspend fun getCurrentLeagueName(): String = getLeagues().first()

    private suspend fun getTradeData(): List<TradeItemID> {
        val response = httpClient.get("https://www.pathofexile.com/api/trade/data/static")
        return response.body<StaticTradeResponse>().result.flatMap { it.entries }
    }

    private suspend fun getCurrencyInfo(league: String? = null): CurrencyOverview {
        val leagueName = league ?: getCurrentLeagueName()
        val response = httpClient.get("https://poe.ninja/api/data/currencyoverview" +
                "?league=$leagueName&type=Currency")
        return response.body()
    }

    suspend fun getCurrencyInChaos(search: String, league: String?): List<CurrencyEquivalent> {
        val currencyOverview = getCurrencyInfo(league)
        val currencies = currencyOverview.currencyDetails

        val adjustedSearch = if (search.trim().equals("gcp", ignoreCase = true)) {
            "gemcutter's prism"
        } else if (search.trim().equals("div", ignoreCase = true)) {
            "divine"
        } else {
            search
        }
        val results = FuzzySearch.extractSorted(adjustedSearch, currencies.map { it.name })
            .filter { it.score > SEARCH_SCORE_THRESHOLD }

        return results.mapNotNull { currencyName ->
            val line = currencyOverview.lines.find { it.currencyTypeName == currencyName.string }
            val usePay = line?.pay?.value != null && line.pay.value > 1
            val paying = currencies.find { details ->
                if (usePay) details.id == line?.pay?.payCurrencyId
                else details.id == line?.receive?.payCurrencyId
            }
            val receiving = currencies.find { details ->
                if (usePay) details.id == line?.pay?.getCurrencyId
                else details.id == line?.receive?.getCurrencyId
            }

            if (paying == null || receiving == null) null
            else {
                CurrencyEquivalent(
                    paying = ItemPrice(
                        name = paying.name,
                        icon = paying.icon,
                        amount = (if (usePay) 1f else line?.receive?.value?.toFloat()) ?: 1f
                    ),
                    receiving = ItemPrice(
                        name = receiving.name,
                        icon = receiving.icon,
                        amount = (if (usePay) line?.pay?.value?.toFloat() else 1f) ?: 1f
                    )
                )
            }
        }
    }

    private suspend fun getBulkPricing(itemId: String, tradeData: List<TradeItemID>? = null): CurrencyEquivalent? {
        val result = httpClient.post {
            url("https://www.pathofexile.com/api/trade/exchange/${getCurrentLeagueName()}")
            contentType(ContentType.Application.Json)
            setBody(BulkTradeRequest.forItem(itemId))
        }

        val tradeOffers = result.body<BulkTradeResponse>().result.entries
            .filter { it.value.listing.offers.isNotEmpty() }
            .take(OFFERS_TO_USE)
            .sortedByDescending { tradeEntry ->
                tradeEntry.value.listing.offers.firstOrNull()?.let { offer ->
                    offer.item.amount / offer.exchange.amount
                } ?: 0f
            }

        if (tradeOffers.isEmpty()) return null

        val median = tradeOffers[(OFFERS_TO_USE / 2).coerceAtMost(tradeOffers.lastIndex)].value
        val offer = median.listing.offers.first()
        return offer.toCurrencyEquivalent(
            payingIcon = tradeData?.find { it.text == offer.exchange.currency }?.image,
            receivingIcon = tradeData?.find { it.text == offer.item.currency }?.image,
        )
    }

    suspend fun searchForBulkPricing(search: String): CurrencyEquivalent? {
        try {
            val tradeData = getTradeData()
            val result = FuzzySearch.extractOne(search, tradeData.map { it.text })

            return getBulkPricing(tradeData[result.index].id, tradeData)
        } catch (error: ClientRequestException) {
            throw error.toReturnableHttpException()
        }
    }

    suspend fun searchForPricing(search: String): CurrencyEquivalent {
        try {
            val initial = httpClient.post {
                url("https://www.pathofexile.com/api/trade/search/${getCurrentLeagueName()}")
                contentType(ContentType.Application.Json)
                setBody(SearchTradeRequest.forSearch(search))
            }
            val tradeIds = initial.body<SearchIdsResponse>().result.take(MAX_FETCH_IDS).joinToString(",")
            val response = httpClient.get("https://www.pathofexile.com/api/trade/fetch/$tradeIds")

            val listings = response.body<SearchTradeResponse>().result
            val median = listings[listings.size / 2]
            return CurrencyEquivalent(
                paying = ItemPrice(
                    name = median.listing.price.currency,
                    amount = median.listing.price.amount.toFloat(),
                    icon = null
                ),
                receiving = ItemPrice(
                    name = median.item?.name.takeUnless { it.orEmpty().isEmpty() }
                        ?: median.item?.typeLine
                        ?: median.item?.baseType.orEmpty(),
                    amount = median.item?.stackSize?.toFloat() ?: 1f,
                    icon = median.item?.icon
                )
            )
        } catch (error: ClientRequestException) {
            throw error.toReturnableHttpException()
        }
    }

    suspend fun searchItemPrice(item: String, league: String? = null): PriceResponse {
        try {
            val leagueName = league ?: getCurrentLeagueName()
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
        const val OFFERS_TO_USE = 8
        const val MAX_FETCH_IDS = 10
    }
}
