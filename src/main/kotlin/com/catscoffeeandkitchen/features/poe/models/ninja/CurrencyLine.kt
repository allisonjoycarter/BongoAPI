package com.catscoffeeandkitchen.features.poe.models.ninja

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyLine(
    val currencyTypeName: String,
    val pay: CurrencyLineDetail? = null,
    val receive: CurrencyLineDetail? = null,
//    val paySparkLine: Any,
//    val receiveSparkLine: Any,
    val chaosEquivalent: Float,
//    val lowConfidenceSparkLine: Any,
//    val lowConfidenceReceiveSparkLine: Any,
    val detailsId: String
)

@Serializable
data class CurrencyLineDetail(
    val id: Int,
    @SerialName("league_id") val leagueId: Int,
    @SerialName("pay_currency_id") val payCurrencyId: Int? = null,
    @SerialName("get_currency_id") val getCurrencyId: Int? = null,
    val sampleTimeUTC: String? = null,
    val count: Int? = null,
    val value: Double? = null,
    @SerialName("data_point_count") val dataPointCount: Int? = null,
    @SerialName("includes_secondary") val includesSecondary: Boolean? = null,
    @SerialName("listing_count") val listingCount: Int? = null
)
