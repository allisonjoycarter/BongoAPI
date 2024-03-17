package com.catscoffeeandkitchen.features.poe.models.ninja

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyOverview(
    val lines: List<CurrencyLine>,
    val currencyDetails: List<CurrencyDetails>
)
