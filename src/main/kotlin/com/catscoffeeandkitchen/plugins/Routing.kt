package com.catscoffeeandkitchen.plugins

import com.catscoffeeandkitchen.features.urban_dictionary.urbanDictionaryRoutes
import com.catscoffeeandkitchen.features.weather.weatherRoutes
import com.catscoffeeandkitchen.features.advice.adviceRoutes
import com.catscoffeeandkitchen.features.facts.factsRoutes
import com.catscoffeeandkitchen.features.jokes.jokeRoutes
import com.catscoffeeandkitchen.features.name_generator.nameGeneratorRoutes
import io.ktor.server.application.*
import io.ktor.server.resources.Resources
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Resources)
    routing {
        adviceRoutes()
        factsRoutes()
        jokeRoutes()
        nameGeneratorRoutes()
        urbanDictionaryRoutes()
        weatherRoutes()
    }
}
