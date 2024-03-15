package com.catscoffeeandkitchen.bongoapi.features.common

import io.ktor.http.*

class ReturnableHttpException(
    val statusCode: HttpStatusCode,
    val responseBody: String? = null,
    message: String? = null
): Exception(message)