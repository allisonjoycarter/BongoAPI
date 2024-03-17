package com.catscoffeeandkitchen.features.poe.models.trade.search

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class SearchTradeRequest(
    val query: SearchQuery,
    @EncodeDefault val engine: String = "new",
    @EncodeDefault val sort: SearchSort? = SearchSort()
) {
    @Serializable
    data class SearchQuery(
        @EncodeDefault val status: AccountStatus = AccountStatus(),
        val stats: List<String> = emptyList(),
        val term: String? = null
    )

    @Serializable
    data class AccountStatus(
        @EncodeDefault val option: String = "online"
    )

    @Serializable
    data class SearchSort(
        @EncodeDefault val price: String = "asc"
    )


    companion object {
        fun forSearch(search: String): SearchTradeRequest = SearchTradeRequest(
            query = SearchQuery(
                term = search
            )
        )
    }
}


