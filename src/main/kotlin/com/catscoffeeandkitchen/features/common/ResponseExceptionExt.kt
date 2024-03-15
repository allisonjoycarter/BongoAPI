package com.catscoffeeandkitchen.bongoapi.features.common

import io.ktor.client.plugins.*
import io.ktor.client.statement.*

suspend fun ResponseException.toReturnableHttpException(): ReturnableHttpException {
    return ReturnableHttpException(
        statusCode = response.status,
        responseBody = response.bodyAsText(),
        message = message
    )
}

