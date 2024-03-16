package com.catscoffeeandkitchen.features.namegenerator

import kotlinx.serialization.Serializable

@Serializable
data class RandomNameResult(
    val names: List<String>
)
