package com.catscoffeeandkitchen.features.advice

import com.aallam.openai.api.exception.OpenAIAPIException
import com.aallam.openai.api.exception.OpenAIException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject

fun Route.adviceRoutes() {
    val repository by inject<AdviceRepository>()

    get<AdviceEndpoint.Random> {
        try {
            val advice = repository.getRandomAdvice()
            call.respondText(advice)
        } catch (error: OpenAIAPIException) {
            call.application.log.error("Received an error getting advice", error)
            call.respond(HttpStatusCode.fromValue(error.statusCode), error.localizedMessage)
        } catch (error: OpenAIException) {
            call.application.log.error("Received an error getting advice", error)
            call.respondText("Stop thinking so hard.")
        }
    }
}
