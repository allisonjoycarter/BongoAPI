package com.catscoffeeandkitchen.features.weather

import com.catscoffeeandkitchen.features.common.ReturnableHttpException
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.util.logging.error
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
            call.application.environment.log.error(httpError)
            call.respond(httpError.statusCode, httpError.responseBody ?: httpError.message ?: "")
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
            call.application.environment.log.error(httpError)
            call.respond(httpError.statusCode, httpError.responseBody ?: httpError.message ?: "")
        }
    }
}
