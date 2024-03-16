package com.catscoffeeandkitchen.features.common

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText


suspend fun ResponseException.toReturnableHttpException(): ReturnableHttpException {
    return ReturnableHttpException(
        statusCode = response.status,
        responseBody = response.bodyAsText(),
        cause = this
    )
}

