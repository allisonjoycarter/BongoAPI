package com.catscoffeeandkitchen.features.poe.models.trade.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchIdsResponse(
    val id: String,
    val complexity: Int,
    val result: List<String>
)
