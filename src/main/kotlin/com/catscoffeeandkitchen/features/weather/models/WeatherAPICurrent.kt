package com.catscoffeeandkitchen.features.weather.models

import com.catscoffeeandkitchen.features.common.UnixEpochDateTimeSerializer
import com.catscoffeeandkitchen.features.weather.models.WeatherCondition
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class WeatherAPICurrent(
    @Serializable(with = UnixEpochDateTimeSerializer::class)
    @SerialName("last_updated_epoch") val lastUpdated: OffsetDateTime,
    @SerialName("last_updated") val lastUpdatedString: String,
    @SerialName("temp_c") val tempC: Float,
    @SerialName("temp_f") val tempF: Float,
    @SerialName("is_day") val isDay: Float,
    val condition: WeatherCondition,
    @SerialName("wind_mph") val windMph: Float,
    @SerialName("wind_kph") val windKph: Float,
    @SerialName("wind_degree") val windDegree: Float,
    @SerialName("wind_dir") val windDir: String,
    @SerialName("pressure_mb") val pressureMb: Float,
    @SerialName("pressure_in") val pressureIn: Float,
    @SerialName("precip_mm") val precipMm: Float,
    @SerialName("precip_in") val precipIn: Float,
    val humidity: Float,
    val cloud: Float,
    @SerialName("feelslike_c") val feelslikeC: Float,
    @SerialName("feelslike_f") val feelslikeF: Float,
    @SerialName("vis_km") val visKm: Float,
    @SerialName("vis_miles") val visMiles: Float,
    val uv: Float,
    @SerialName("gust_mph") val gustMph: Float,
    @SerialName("gust_kph") val gustKph: Float
)
