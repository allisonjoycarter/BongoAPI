package com.catscoffeeandkitchen.features.poe.models.trade

import kotlinx.serialization.Serializable

@Serializable
data class StaticTradeResponse(
    val result: List<Section>
) {
    @Serializable
    data class Section(
        val id: String = "",
        val label: String? = null,
        val entries: List<TradeItemID> = emptyList()
    )
}
