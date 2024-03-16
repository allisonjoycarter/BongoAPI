package com.catscoffeeandkitchen.features.jokes

import com.catscoffeeandkitchen.features.common.ReturnableHttpException
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.util.logging.error
import org.koin.ktor.ext.inject

fun Route.jokeRoutes() {
    val jokesDataSource by inject<JokeRepository>()

    get<JokeEndpoint.Random> {
        try {
            call.respondText(jokesDataSource.getJoke(JokeCategory.Any))
        } catch (httpError: ReturnableHttpException) {
            call.application.environment.log.error(httpError)
            call.response.status(httpError.statusCode)
        }
    }

    get<JokeEndpoint.Category> { joke  ->
        try {
            val category = JokeCategory.values().find {
                it.name.equals(joke.category, ignoreCase = true)
            } ?: JokeCategory.Any

            call.respondText(jokesDataSource.getJoke(category))
        } catch (httpError: ReturnableHttpException) {
            call.application.environment.log.error(httpError)
            call.response.status(httpError.statusCode)
        }
    }
}
