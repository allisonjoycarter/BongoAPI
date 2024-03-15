package com.catscoffeeandkitchen.features.facts

import com.catscoffeeandkitchen.bongoapi.features.common.toReturnableHttpException
import com.catscoffeeandkitchen.bongoapi.features.facts.RandomFactResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

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