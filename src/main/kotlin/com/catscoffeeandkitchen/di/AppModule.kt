package com.catscoffeeandkitchen.di

import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.catscoffeeandkitchen.features.advice.AdviceRepository
import com.catscoffeeandkitchen.features.facts.FactsRepository
import com.catscoffeeandkitchen.features.jokes.JokeRepository
import com.catscoffeeandkitchen.features.name_generator.NameGeneratorRepository
import com.catscoffeeandkitchen.features.urban_dictionary.UrbanDictionaryRepository
import com.catscoffeeandkitchen.features.weather.WeatherRepository
import com.typesafe.config.ConfigFactory
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val appModule = module {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val openAIKey = config.propertyOrNull("api.open_ai")?.getString()
    val isDebug = config.propertyOrNull("env")?.getString() == "dev"

    single {
        HttpClient(CIO) {
            developmentMode = isDebug
            expectSuccess = true

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = isDebug
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single {
        openAIKey?.let { token ->
            OpenAI(
                config = OpenAIConfig(
                    token = token,
                    logging = LoggingConfig(logLevel = if (isDebug) LogLevel.Body else LogLevel.Headers),
                )
            )
        }
    }

    single {
        AdviceRepository(openAIClient = get())
    }
    single {
        FactsRepository(httpClient = get())
    }
    single {
        JokeRepository(httpClient = get())
    }
    single {
        NameGeneratorRepository(openAIClient = get())
    }
    single {
        UrbanDictionaryRepository(httpClient = get())
    }
    single {
        WeatherRepository(
            apiKey = config.propertyOrNull("api.weather")?.getString().orEmpty(),
            httpClient = get()
        )
    }
}
