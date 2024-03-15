package com.catscoffeeandkitchen.bongoapi.features.weather.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherAPIResponse(
    val location: Location,
    val current: WeatherAPICurrent,
    val forecast: WeatherAPIForecast? = null,
)
