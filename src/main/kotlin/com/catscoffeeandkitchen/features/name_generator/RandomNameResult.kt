package com.catscoffeeandkitchen.bongoapi.features.name_generator

import kotlinx.serialization.Serializable

@Serializable
data class RandomNameResult(
    val names: List<String>
)
