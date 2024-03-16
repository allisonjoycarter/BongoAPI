package com.catscoffeeandkitchen.features.weather.models

import com.catscoffeeandkitchen.features.common.UnixEpochDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class WeatherAPIForecast(
    val forecastday: List<WeatherAPIForecastDay>
)

@Serializable
data class WeatherAPIForecastDay(
    val date: String,
    @Serializable(with = UnixEpochDateTimeSerializer::class) @SerialName("date_epoch") val dateEpoch: OffsetDateTime,
    val day: WeatherAPIDay
)

@Serializable
data class WeatherAPIDay(
    @SerialName("maxtemp_c") val maxTempC: Float,
    @SerialName("mintemp_c") val minTempC: Float,
    @SerialName("avgtemp_c") val averageTempC: Float,
    @SerialName("maxtemp_f") val maxTempF: Float,
    @SerialName("mintemp_f") val minTempF: Float,
    @SerialName("avgtemp_f") val averageTempF: Float,
    @SerialName("totalprecip_in") val precipInInches: Float,
    @SerialName("totalsnow_cm") val snowInCM: Float,
    @SerialName("avgvis_km") val visibilityInKM: Float,
    @SerialName("avgvis_miles") val visibilityInMI: Float,
    @SerialName("avghumidity") val humidity: Int,

    @Serializable(with = IntToBoolSerializer::class)
    @SerialName("daily_will_it_rain") val willItRain: Boolean,

    @SerialName("daily_chance_of_rain") val chanceOfRain: Int,

    @Serializable(with = IntToBoolSerializer::class)
    @SerialName("daily_will_it_snow") val willItSnow: Boolean,

    @SerialName("daily_chance_of_snow") val chanceOfSnow: Int,
    val condition: WeatherCondition,
    val uv: Float
)
