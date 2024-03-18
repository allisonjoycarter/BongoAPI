package com.catscoffeeandkitchen.features.common

import io.ktor.http.HttpStatusCode

class ReturnableHttpException(
    val statusCode: HttpStatusCode,
    val origin: String,
    val responseBody: String? = null,
    cause: Throwable? = null
): Throwable(cause = cause) {
    override val message: String
        get() = "Received ${statusCode.value} ${statusCode.description} from $origin - $responseBody"
}
