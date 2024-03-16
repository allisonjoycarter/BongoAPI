package com.catscoffeeandkitchen.features.weather

import com.catscoffeeandkitchen.features.common.toReturnableHttpException
import com.catscoffeeandkitchen.features.weather.models.Forecast
import com.catscoffeeandkitchen.features.weather.models.WeatherAPIResponse
import com.catscoffeeandkitchen.features.weather.models.WeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.path

class WeatherRepository(
    private val apiKey: String,
    private val httpClient: HttpClient
) {

    suspend fun getWeatherAt(location: String, days: Int = 1): WeatherResponse {
        try {
            val response = httpClient.get {
                url {
                    protocol = URLProtocol.HTTP
                    host = "api.weatherapi.com"
                    path("v1", if (days == 1) "current.json" else "forecast.json")

                    parameters.append("key", apiKey)
                    parameters.append("q", location)
                    parameters.append("aqi", "no")
                    @Suppress("MagicNumber")
                    if (days > 1) parameters.append("days", days.coerceIn(1, 3).toString())
                }

                headers {
                    append(HttpHeaders.Accept, "application/json")
                }
            }
            val weatherData = response.body<WeatherAPIResponse>()

            return WeatherResponse(
                location = weatherData.location.name,
                tempC = weatherData.current.tempC,
                tempF = weatherData.current.tempF,
                condition = weatherData.current.condition,
                humidity = weatherData.current.humidity,
                windspeed = weatherData.current.windMph,
                lastUpdated = weatherData.current.lastUpdated,
                forecast = if (days == 1) null else weatherData.forecast?.forecastday.orEmpty().map { day ->
                    Forecast(
                        date = day.dateEpoch,
                        maxTempC = day.day.maxTempC,
                        minTempC = day.day.minTempC,
                        avgTempC = day.day.averageTempC,
                        maxTempF = day.day.maxTempF,
                        minTempF = day.day.minTempF,
                        avgTempF = day.day.averageTempF,
                        condition = day.day.condition,
                        precipitation = day.day.precipInInches,
                        snow = (day.day.snowInCM * 0.3937007874).toFloat(),
                        raining = day.day.willItRain,
                        snowing = day.day.willItSnow
                    )
                }
            )
        } catch (error: ResponseException) {
            throw error.toReturnableHttpException()
        }
    }
}
