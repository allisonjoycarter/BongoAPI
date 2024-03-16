package com.catscoffeeandkitchen.features.urbandictionary

import kotlinx.serialization.Serializable

@Serializable
data class DefineResponse(
    val list: List<UrbanDefinition>
)
