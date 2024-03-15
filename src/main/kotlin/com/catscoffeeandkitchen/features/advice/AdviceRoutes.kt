package com.catscoffeeandkitchen.features.advice

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.adviceRoutes() {
    val repository by inject<AdviceRepository>()

    get<AdviceEndpoint.Random> {
        try {
            val advice = repository.getRandomAdvice()
            call.respondText(advice)
        } catch (error: Exception) {
            logError(call, error)
            call.respondText("Stop thinking so hard.")
        }
    }
}
