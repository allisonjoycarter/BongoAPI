package com.catscoffeeandkitchen.features.name_generator

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import org.koin.ktor.ext.inject

fun Route.nameGeneratorRoutes() {
    val repository by inject<NameGeneratorRepository>()

    get<NameGeneratorEndpoint.Category> { request ->
        try {
            val type = NameCategory.values().find {
                it.name.equals(request.category, ignoreCase = true)
            } ?: NameCategory.values().random()

            call.respond(repository.generateName(type, request.context, request.amount))
        } catch (error: Exception) {
            call.application.environment.log.error(error)
            call.response.status(HttpStatusCode(500, "Unexpected error."))
        }
    }

}
