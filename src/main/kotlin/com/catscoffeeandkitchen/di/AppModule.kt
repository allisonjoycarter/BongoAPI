package com.catscoffeeandkitchen.di

import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.catscoffeeandkitchen.features.advice.AdviceRepository
import com.catscoffeeandkitchen.features.facts.FactsRepository
import com.catscoffeeandkitchen.features.jokes.JokeRepository
import com.catscoffeeandkitchen.features.namegenerator.NameGeneratorRepository
import com.catscoffeeandkitchen.features.poe.PoeBaseData
import com.catscoffeeandkitchen.features.poe.PoeBaseDataCache
import com.catscoffeeandkitchen.features.poe.PoeBaseDataRepository
import com.catscoffeeandkitchen.features.poe.PoeRepository
import com.catscoffeeandkitchen.features.urbandictionary.UrbanDictionaryRepository
import com.catscoffeeandkitchen.features.weather.WeatherRepository
import com.typesafe.config.ConfigFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.HoconApplicationConfig
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import java.io.File

val appModule = module {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val openAIKey = config.propertyOrNull("api.open_ai")?.getString()
    val isDebug = config.propertyOrNull("env")?.getString() == "dev"

    single {
        HttpClient(CIO) {
            developmentMode = isDebug
            expectSuccess = true

            install(Logging) {
                logger = Logger.DEFAULT
                level = if (isDebug) io.ktor.client.plugins.logging.LogLevel.ALL
                        else io.ktor.client.plugins.logging.LogLevel.INFO
            }

            install(HttpCache)
            install(HttpCookies)
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = isDebug
                    ignoreUnknownKeys = true
                })
            }

            // alternatively, BrowserUserAgent()
            install(UserAgent) {
                agent = "Bongo API/1.0 - bravebongo.com/about"
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
    single<PoeBaseData> {
        PoeBaseDataCache(
            PoeBaseDataRepository(
                httpClient = get()
            ),
            File(config.property("storage.ehcacheFilePath").getString())
        )
    }
    single {
        PoeRepository(
            httpClient = get(),
            baseData = get()
        )
    }
}
