package com.catscoffeeandkitchen.features.weather.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Float,
    val lon: Float,
    @SerialName("tz_id") val timezone: String,
    @SerialName("localtime_epoch") val localtimeEpoch: Long,
    val localtime: String
)
