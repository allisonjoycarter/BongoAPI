package com.catscoffeeandkitchen.features.urban_dictionary

import com.catscoffeeandkitchen.bongoapi.features.common.ReturnableHttpException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import org.koin.ktor.ext.inject


fun Route.urbanDictionaryRoutes() {
    val repository by inject<UrbanDictionaryRepository>()

    get<UrbanDictionaryEndpoint.Define> { request ->
        try {
            call.respond(repository.define(request.term))
        } catch (error: ReturnableHttpException) {
            call.application.environment.log.error(error)
            call.response.status(error.statusCode)
        } catch (error: Exception) {
            call.application.environment.log.error(error)
            call.response.status(HttpStatusCode(500, "Unexpected error."))
        }
    }

    get<UrbanDictionaryEndpoint.Random> {
        try {
            call.respond(repository.random())
        } catch (error: ReturnableHttpException) {
            call.application.environment.log.error(error)
            call.response.status(error.statusCode)
        } catch (error: Exception) {
            call.application.environment.log.error(error)
            call.response.status(HttpStatusCode(500, "Unexpected error."))
        }
    }
}
