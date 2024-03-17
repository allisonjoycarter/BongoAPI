package com.catscoffeeandkitchen.features.poe.models.trade.bulk

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class BulkTradeRequest(
    val query: BulkQuery,
    @EncodeDefault val engine: String = "new",
    @EncodeDefault val sort: Sort? = Sort()
) {
    @Serializable
    data class BulkQuery(
        val want: List<String>,
        @EncodeDefault val status: StatusOption = StatusOption(),
        val have: List<String> = emptyList()
    )

    @Serializable
    data class StatusOption(
        @EncodeDefault val option: String = "online"
    )

    @Serializable
    data class Sort(
        @EncodeDefault val have: String = "asc",
    )

    companion object {
        fun forItem(tag: String): BulkTradeRequest = BulkTradeRequest(
            query = BulkQuery(
                want = listOf(tag),
                status = StatusOption(option = "online")
            ),
            sort = null
        )
    }
}


