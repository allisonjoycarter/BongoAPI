package com.catscoffeeandkitchen.features.poe.models

import kotlinx.serialization.Serializable

@Serializable
data class PriceResponse(
    val min: Float,
    val max: Float,
    val currency: String,
    val confidence: Float,
    val warning: String? = null,
    val error: String? = null
)
