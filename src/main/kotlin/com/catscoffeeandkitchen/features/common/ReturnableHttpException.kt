package com.catscoffeeandkitchen.features.common

import io.ktor.http.HttpStatusCode

class ReturnableHttpException(
    val statusCode: HttpStatusCode,
    val responseBody: String? = null,
    cause: Throwable? = null
): Exception(cause = cause)
