package com.catscoffeeandkitchen.features.poe.models.trade.bulk

import com.catscoffeeandkitchen.features.poe.models.trade.Account
import kotlinx.serialization.Serializable

@Serializable
data class BulkTradeListing(
    val indexed: String = "",
    val account: Account,
    val offers: List<BulkTradeResponse.Offer> = emptyList(),
    val whisper: String? = null
)

