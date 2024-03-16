package com.catscoffeeandkitchen.features.urbandictionary

import com.catscoffeeandkitchen.features.common.ReturnableHttpException
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.util.logging.error
import org.koin.ktor.ext.inject


fun Route.urbanDictionaryRoutes() {
    val repository by inject<UrbanDictionaryRepository>()

    get<UrbanDictionaryEndpoint.Define> { request ->
        try {
            call.respond(repository.define(request.term))
        } catch (error: ReturnableHttpException) {
            call.application.environment.log.error(error)
            call.response.status(error.statusCode)
        }
    }

    get<UrbanDictionaryEndpoint.Random> {
        try {
            call.respond(repository.random())
        } catch (error: ReturnableHttpException) {
            call.application.environment.log.error(error)
            call.response.status(error.statusCode)
        }
    }
}
