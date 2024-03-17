package com.catscoffeeandkitchen.features.poe.models.ninja

import kotlinx.serialization.Serializable

@Serializable
data class EconomyLeague(
    val name: String,
    val url: String,
    val displayName: String,
    val hardcore: Boolean,
    val indexed: Boolean
)
