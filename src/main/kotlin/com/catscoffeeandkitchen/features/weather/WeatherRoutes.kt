package com.catscoffeeandkitchen.features.weather

import com.catscoffeeandkitchen.bongoapi.features.common.ReturnableHttpException
import com.catscoffeeandkitchen.features.weather.WeatherEndpoint
import com.catscoffeeandkitchen.features.weather.WeatherRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import io.ktor.util.logging.*
import org.koin.ktor.ext.inject

fun Route.weatherRoutes() {
    val weatherRepository by inject<WeatherRepository>()

    get<WeatherEndpoint.Current> { weather ->
        try {
            val forecast = weatherRepository.getWeatherAt(
                location = weather.location ?: "grand rapids",
                days = 1
            )

            call.respond(forecast)
        } catch (httpError: ReturnableHttpException) {
            call.respond(httpError.statusCode, httpError.responseBody ?: httpError.message ?: "")
        } catch (error: Exception) {
            call.application.environment.log.error(error)
            call.response.status(HttpStatusCode(500, "Unexpected error."))
        }
    }

    get<WeatherEndpoint.Forecast> { weather ->
        try {
            val forecast = weatherRepository.getWeatherAt(
                weather.location ?: "grand rapids",
                weather.days
            )

            call.respond(forecast)
        } catch (httpError: ReturnableHttpException) {
            call.respond(httpError.statusCode, httpError.responseBody ?: httpError.message ?: "")
        } catch (error: Exception) {
            call.application.environment.log.error(error)
            call.response.status(HttpStatusCode(500, "Unexpected error."))
        }
    }
}