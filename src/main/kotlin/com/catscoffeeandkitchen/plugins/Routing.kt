package com.catscoffeeandkitchen.plugins

import com.catscoffeeandkitchen.features.urbandictionary.urbanDictionaryRoutes
import com.catscoffeeandkitchen.features.weather.weatherRoutes
import com.catscoffeeandkitchen.features.advice.adviceRoutes
import com.catscoffeeandkitchen.features.facts.factsRoutes
import com.catscoffeeandkitchen.features.jokes.jokeRoutes
import com.catscoffeeandkitchen.features.namegenerator.nameGeneratorRoutes
import com.catscoffeeandkitchen.features.poe.poeRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.resources.Resources
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    install(Resources)
    routing {
        jokeRoutes()

        authenticate("auth-api-key") {
            adviceRoutes()
            factsRoutes()
            nameGeneratorRoutes()
            urbanDictionaryRoutes()
            weatherRoutes()
            poeRoutes()
        }
    }
}
