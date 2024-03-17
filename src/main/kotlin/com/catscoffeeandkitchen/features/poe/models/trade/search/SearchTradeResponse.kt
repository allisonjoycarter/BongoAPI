package com.catscoffeeandkitchen.features.poe.models.trade.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchTradeResponse(
    val id: String? = null,
    val complexity: Int? = null,
    val result: List<TradeResult>
) {
    @Serializable
    data class TradeResult(
        val id: String,
        val item: TradeItem? = null,
        val listing: SearchListing
    )
}
