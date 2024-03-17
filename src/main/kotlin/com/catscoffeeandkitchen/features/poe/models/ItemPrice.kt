package com.catscoffeeandkitchen.features.poe.models

import kotlinx.serialization.Serializable

@Serializable
data class ItemPrice(
    val name: String,
    val icon: String?,
    val amount: Float
)
