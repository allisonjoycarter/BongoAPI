package com.catscoffeeandkitchen.features.urban_dictionary

import com.catscoffeeandkitchen.bongoapi.features.common.toReturnableHttpException
import com.catscoffeeandkitchen.bongoapi.features.urban_dictionary.DefineResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*

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