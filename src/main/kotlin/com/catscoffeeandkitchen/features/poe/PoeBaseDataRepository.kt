package com.catscoffeeandkitchen.features.poe

import com.catscoffeeandkitchen.features.poe.models.ninja.CurrencyOverview
import com.catscoffeeandkitchen.features.poe.models.ninja.IndexState
import com.catscoffeeandkitchen.features.poe.models.trade.StaticTradeResponse
import com.catscoffeeandkitchen.features.poe.models.trade.TradeItemID
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PoeBaseDataRepository(
    private val httpClient: HttpClient
): PoeBaseData {
    private suspend fun getLeagues(): List<String> {
        val response = httpClient.get("https://poe.ninja/api/data/getindexstate")
        return response.body<IndexState>().economyLeagues.map { it.displayName }
    }

    override suspend fun getCurrentLeagueName(): String = getLeagues().first()

    override suspend fun getTradeData(): List<TradeItemID> {
        val response = httpClient.get("https://www.pathofexile.com/api/trade/data/static")
        return response.body<StaticTradeResponse>().result.flatMap { it.entries }
    }

    override suspend fun getCurrencyInfo(): CurrencyOverview {
        val leagueName = getCurrentLeagueName()
        val response = httpClient.get("https://poe.ninja/api/data/currencyoverview" +
                "?league=$leagueName&type=Currency")
        return response.body()
    }
}
