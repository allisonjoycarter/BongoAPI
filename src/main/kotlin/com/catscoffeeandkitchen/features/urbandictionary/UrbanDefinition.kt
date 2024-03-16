package com.catscoffeeandkitchen.features.urbandictionary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UrbanDefinition(
    val definition: String? = null,
    val permalink: String? = null,
    @SerialName("thumbs_up") val thumbsUp: Int? = null,
    val author: String? = null,
    val word: String? = null,
    val defid: Int? = null,
    @SerialName("current_vote") val currentVote: String? = null,
    @SerialName("written_on") val writtenOn: String? = null,
    val example: String? = null,
    @SerialName("thumbs_down") val thumbsDown: Int? = null
)
