package com.catscoffeeandkitchen.features.facts

import com.catscoffeeandkitchen.bongoapi.features.common.ReturnableHttpException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import org.koin.ktor.ext.inject

fun Route.factsRoutes() {
    val factsRepository by inject<FactsRepository>()

    get<FactEndpoint.Random> {
        try {
            val fact = factsRepository.getRandomFact()
            call.respondText(fact)
        } catch (httpError: ReturnableHttpException) {
            call.response.status(httpError.statusCode)
        } catch (error: Exception) {
            call.application.environment.log.error(error)
            call.response.status(HttpStatusCode(500, "Unexpected error."))
        }
    }
}
