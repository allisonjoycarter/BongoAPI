package com.catscoffeeandkitchen.plugins

import com.catscoffeeandkitchen.features.urbandictionary.urbanDictionaryRoutes
import com.catscoffeeandkitchen.features.weather.weatherRoutes
import com.catscoffeeandkitchen.features.advice.adviceRoutes
import com.catscoffeeandkitchen.features.facts.factsRoutes
import com.catscoffeeandkitchen.features.jokes.jokeRoutes
import com.catscoffeeandkitchen.features.namegenerator.nameGeneratorRoutes
import com.catscoffeeandkitchen.features.poe.poeRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.resources.Resources
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    install(Resources)
    routing {
        get("/") {
            call.respondText("OK")
        }

        get("/about") {
            call.respondText("For support, contact doubleterrainbow@gmail.com.")
        }

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
