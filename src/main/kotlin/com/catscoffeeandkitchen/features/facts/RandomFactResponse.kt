package com.catscoffeeandkitchen.bongoapi.features.facts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RandomFactResponse(
    val id: String,
    val text: String,
    val source: String?,
    @SerialName("source_url") val sourceUrl: String?,
    val language: String?,
    val permalink: String?
)
