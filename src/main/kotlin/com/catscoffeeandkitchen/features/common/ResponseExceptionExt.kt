package com.catscoffeeandkitchen.features.common

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request


suspend fun ResponseException.toReturnableHttpException(): ReturnableHttpException {
    return ReturnableHttpException(
        statusCode = response.status,
        responseBody = response.bodyAsText(),
        origin = response.request.url.host,
        cause = this
    )
}

