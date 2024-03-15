package com.catscoffeeandkitchen.features.jokes

import com.catscoffeeandkitchen.bongoapi.features.common.ReturnableHttpException
import com.catscoffeeandkitchen.bongoapi.features.jokes.JokeCategory
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class JokeRepository(
    val httpClient: HttpClient
) {
    suspend fun getJoke(category: JokeCategory): String {
        try {
            val result = httpClient.get("https://v2.jokeapi.dev/joke/${category.name}?format=txt&type=single")
            return result.bodyAsText()
        } catch (error: ResponseException) {
            throw ReturnableHttpException(
                statusCode = error.response.status,
                responseBody = error.response.bodyAsText(),
                message = error.message
            )
        }
    }
}