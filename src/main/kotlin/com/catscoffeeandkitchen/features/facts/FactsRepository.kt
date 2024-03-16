package com.catscoffeeandkitchen.features.facts

import com.catscoffeeandkitchen.features.common.toReturnableHttpException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders

class FactsRepository(private val httpClient: HttpClient) {

    suspend fun getRandomFact(): String {
        try {
            val result = httpClient.get("https://uselessfacts.jsph.pl/api/v2/facts/random?language=en") {
                headers {
                    append(HttpHeaders.Accept, "application/json")
                }
            }

            val fact = result.body<RandomFactResponse>()
            return fact.text
        } catch (error: ResponseException) {
            throw error.toReturnableHttpException()
        }
    }
}
