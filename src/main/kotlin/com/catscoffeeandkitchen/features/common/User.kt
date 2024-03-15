package com.catscoffeeandkitchen.bongoapi.features.common

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val password: String
)
