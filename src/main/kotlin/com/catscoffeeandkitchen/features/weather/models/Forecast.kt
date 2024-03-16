package com.catscoffeeandkitchen.features.weather.models

import com.catscoffeeandkitchen.features.common.UnixEpochDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class Forecast(
    @Serializable(with = UnixEpochDateTimeSerializer::class)
    val date: OffsetDateTime,
    val maxTempC: Float,
    val minTempC: Float,
    val avgTempC: Float,
    val maxTempF: Float,
    val minTempF: Float,
    val avgTempF: Float,
    val condition: WeatherCondition,
    val precipitation: Float,
    val snow: Float,
    val raining: Boolean,
    val snowing: Boolean
)
