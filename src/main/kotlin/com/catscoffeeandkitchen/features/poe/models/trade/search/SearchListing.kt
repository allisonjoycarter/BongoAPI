package com.catscoffeeandkitchen.features.poe.models.trade.search

import com.catscoffeeandkitchen.features.poe.models.trade.Account
import kotlinx.serialization.Serializable

@Serializable
data class SearchListing(
    val indexed: String = "",
    val account: Account,
    val price: Price
) {
    @Serializable
    data class Price(
        val type: String = "",
        val amount: Int = 1,
        val currency: String = "chaos"
    )
}
