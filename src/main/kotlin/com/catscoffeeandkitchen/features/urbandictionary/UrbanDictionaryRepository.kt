package com.catscoffeeandkitchen.features.urbandictionary

import com.catscoffeeandkitchen.features.common.toReturnableHttpException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get

class UrbanDictionaryRepository(private val httpClient: HttpClient) {

    suspend fun define(term: String): List<UrbanDefinition> {
        try {
            val response = httpClient.get("https://api.urbandictionary.com/v0/define?term=$term")
            val definitions = response.body<DefineResponse>()

            return definitions.list
        } catch (error: ResponseException) {
            throw error.toReturnableHttpException()
        }
    }

    suspend fun random(): List<UrbanDefinition> {
        try {
            val response = httpClient.get("https://api.urbandictionary.com/v0/random")
            val definitions = response.body<DefineResponse>()

            return definitions.list
        } catch (error: ResponseException) {
            throw error.toReturnableHttpException()
        }
    }
}
