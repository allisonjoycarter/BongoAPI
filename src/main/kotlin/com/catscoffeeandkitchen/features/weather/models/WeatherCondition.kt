package com.catscoffeeandkitchen.bongoapi.features.weather.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherCondition(
    val text: String,
    val icon: String,
    val code: Int
)
