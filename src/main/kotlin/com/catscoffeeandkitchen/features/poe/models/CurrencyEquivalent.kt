package com.catscoffeeandkitchen.features.poe.models

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyEquivalent(
    val paying: ItemPrice,
    val receiving: ItemPrice
)
