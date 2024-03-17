package com.catscoffeeandkitchen.features.poe.models.trade.bulk

import com.catscoffeeandkitchen.features.poe.models.CurrencyEquivalent
import com.catscoffeeandkitchen.features.poe.models.ItemPrice
import com.catscoffeeandkitchen.features.poe.models.trade.search.TradeItem
import kotlinx.serialization.Serializable

@Serializable
data class BulkTradeResponse(
    val id: String,
    val complexity: Int? = null,
    val result: Map<String, TradeResult>
) {
    @Serializable
    data class TradeResult(
        val id: String,
        val item: TradeItem? = null,
        val listing: BulkTradeListing
    )

    @Serializable
    data class Offer(
        val exchange: Exchange,
        val item: PriceDetails
    ) {
        @Serializable
        data class Exchange(
            val currency: String = "chaos",
            val amount: Float = 1f,
            val stock: Int? = null,
            val whisper: String? = null
        )

        @Serializable
        data class PriceDetails(
            val currency: String = "chaos",
            val amount: Float = 1f,
            val stock: Int? = null,
            val id: String? = null,
            val whisper: String? = null
        )

        fun toCurrencyEquivalent(
            payingIcon: String? = null,
            receivingIcon: String? = null
        ): CurrencyEquivalent {
            return CurrencyEquivalent(
                paying = ItemPrice(
                    name = exchange.currency
                        .replace("-", " ")
                        .split(" ")
                        .joinToString(" ") { it.replaceFirstChar { ch -> ch.uppercase() } },
                    amount = exchange.amount,
                    icon = payingIcon
                ),
                receiving = ItemPrice(
                    name = item.currency
                        .replace("-", " ")
                        .split(" ")
                        .joinToString(" ") { it.replaceFirstChar { ch -> ch.uppercase() } },
                    amount = item.amount,
                    icon = receivingIcon
                )
            )
        }
    }
}
