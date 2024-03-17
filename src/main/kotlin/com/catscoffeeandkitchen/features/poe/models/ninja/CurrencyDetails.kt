package com.catscoffeeandkitchen.features.poe.models.ninja

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyDetails(
    val id: Int,
    val name: String,
    val icon: String? = null,
    val tradeId: String? = null
)
