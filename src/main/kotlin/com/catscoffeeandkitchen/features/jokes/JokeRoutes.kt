package com.catscoffeeandkitchen.features.jokes

import com.catscoffeeandkitchen.bongoapi.features.common.ReturnableHttpException
import com.catscoffeeandkitchen.bongoapi.features.jokes.JokeCategory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import org.koin.ktor.ext.inject

fun Route.jokeRoutes() {
    val jokesDataSource by inject<JokeRepository>()

    get<JokeEndpoint.Random> {
        try {
            call.respondText(jokesDataSource.getJoke(JokeCategory.Any))
        } catch (httpError: ReturnableHttpException) {
            call.response.status(httpError.statusCode)
        } catch (error: Exception) {
            call.application.environment.log.error(error)
            call.response.status(HttpStatusCode(500, "Unexpected error."))
        }
    }

    get<JokeEndpoint.Category> { joke  ->
        try {
            val category = JokeCategory.values().find {
                it.name.equals(joke.category, ignoreCase = true)
            } ?: JokeCategory.Any

            call.respondText(jokesDataSource.getJoke(category))
        } catch (httpError: ReturnableHttpException) {
            call.response.status(httpError.statusCode)
        } catch (error: Exception) {
            call.application.environment.log.error(error)
            call.response.status(HttpStatusCode(500, "Unexpected error."))
        }
    }
}