package com.catscoffeeandkitchen.bongoapi.features.urban_dictionary

import com.catscoffeeandkitchen.features.urban_dictionary.UrbanDefinition
import kotlinx.serialization.Serializable

@Serializable
data class DefineResponse(
    val list: List<UrbanDefinition>
)
