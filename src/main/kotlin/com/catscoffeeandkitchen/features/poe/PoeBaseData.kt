package com.catscoffeeandkitchen.features.poe

import com.catscoffeeandkitchen.features.poe.models.ninja.CurrencyOverview
import com.catscoffeeandkitchen.features.poe.models.trade.TradeItemID

interface PoeBaseData {
    suspend fun getCurrentLeagueName(): String
    suspend fun getTradeData(): List<TradeItemID>
    suspend fun getCurrencyInfo(): CurrencyOverview
}
