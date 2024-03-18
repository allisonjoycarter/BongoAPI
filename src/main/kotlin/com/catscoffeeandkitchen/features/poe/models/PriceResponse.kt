package com.catscoffeeandkitchen.features.poe.models

import kotlinx.serialization.Serializable

@Serializable
data class PriceResponse(
    val name: String,
    val min: Float,
    val max: Float,
    val currency: String,
    val confidence: Float,
    val tradeUrl: String? = null,
    val image: String? = null,
    val warning: String? = null,
    val error: String? = null
)
