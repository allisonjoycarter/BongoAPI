package com.catscoffeeandkitchen.features.facts

import com.catscoffeeandkitchen.features.common.ReturnableHttpException
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.util.logging.error
import org.koin.ktor.ext.inject

fun Route.factsRoutes() {
    val factsRepository by inject<FactsRepository>()

    get<FactEndpoint.Random> {
        try {
            val fact = factsRepository.getRandomFact()
            call.respondText(fact)
        } catch (httpError: ReturnableHttpException) {
            call.application.environment.log.error(httpError)
            call.response.status(httpError.statusCode)
        }
    }
}
