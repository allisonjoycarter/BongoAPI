package com.catscoffeeandkitchen.features.poe.models.trade

import kotlinx.serialization.Serializable

@Serializable
data class TradeItemID(
    val id: String,
    val text: String,
    val image: String? = null
)
