package com.catscoffeeandkitchen.features.poe.models.trade

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val name: String,
    // val online: BulkTradeResponse.LeagueStatus?, can also be false ????
    val lastCharacterName: String? = null,
    val language: String? = null,
    val realm: String? = null
)
