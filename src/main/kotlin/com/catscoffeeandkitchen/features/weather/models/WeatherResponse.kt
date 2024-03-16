package com.catscoffeeandkitchen.features.weather.models

import com.catscoffeeandkitchen.features.common.UnixEpochDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class WeatherResponse(
    val location: String,
    val tempC: Float,
    val tempF: Float,
    val condition: WeatherCondition,
    val humidity: Float,
    val windspeed: Float,
    @Serializable(with = UnixEpochDateTimeSerializer::class) val lastUpdated: OffsetDateTime,
    val forecast: List<Forecast>? = null
)
