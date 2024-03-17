package com.catscoffeeandkitchen.features.poe.models.poeprices

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceInfoResult(
    val min: Float,
    val max: Float,
    val currency: String = "chaos",
    @SerialName("warning_msg") val warningMessage: String = "",
    val error: Int = 0,
    @SerialName("pred_explanation") val predictionExplanation: List<List<String>>,
    @SerialName("pred_confidence_score") val confidence: Double,
    @SerialName("error_msg") val errorMessage: String = ""
)
