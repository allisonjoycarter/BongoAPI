package com.catscoffeeandkitchen.features.namegenerator

import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject

fun Route.nameGeneratorRoutes() {
    val repository by inject<NameGeneratorRepository>()

    get<NameGeneratorEndpoint.Category> { request ->
        @Suppress("SwallowedException")
        val category = try {
            NameCategory.valueOf(request.category)
        } catch (error: IllegalArgumentException) {
            NameCategory.values().random()
        }

        call.respond(repository.generateName(category, request.context, request.amount))
    }

}
