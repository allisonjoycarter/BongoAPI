package com.catscoffeeandkitchen.features.jokes

import com.catscoffeeandkitchen.features.common.toReturnableHttpException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class JokeRepository(
    val httpClient: HttpClient
) {
    suspend fun getJoke(category: JokeCategory): String {
        try {
            val result = httpClient.get("https://v2.jokeapi.dev/joke/${category.name}?format=txt&type=single")
            return result.bodyAsText()
        } catch (error: ResponseException) {
            throw error.toReturnableHttpException()
        }
    }
}
