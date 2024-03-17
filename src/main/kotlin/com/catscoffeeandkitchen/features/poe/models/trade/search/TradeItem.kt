package com.catscoffeeandkitchen.features.poe.models.trade.search

import kotlinx.serialization.Serializable


@Serializable
data class TradeItem(
    val verified: Boolean? = null,
    val w: Int? = null,
    val h: Int? = null,
    val icon: String? = null,
    val stackSize: Int? = null,
    val maxStackSize: Int? = null,
    val league: String? = null,
    val id: String,
    val name: String,
    val typeLine: String,
    val baseType: String? = null,
    val ilvl: Int? = null,
    val identified: Boolean? = null,
    val properties: List<Property>? = null,
    val explicitMods: List<String>? = null,
    val descrText: String? = null
//    val extended: BigObject
) {
    @Serializable
    data class Property(
        val name: String,
//        val values: List<List<StringOrInt>>
        val displayMode: Int? = null,
        val type: Int? = null
    )
}
