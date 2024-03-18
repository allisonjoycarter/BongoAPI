package com.catscoffeeandkitchen.features.poe.models.poeprices

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceInfoResult(
    val min: Float = 0f,
    val max: Float = 0f,
    val currency: String = "chaos",
    @SerialName("warning_msg") val warningMessage: String = "",
    val error: Int = 0,
    @SerialName("pred_explanation") val predictionExplanation: List<List<String>> = emptyList(),
    @SerialName("pred_confidence_score") val confidence: Double = 0.0,
    @SerialName("error_msg") val errorMessage: String = ""
)
