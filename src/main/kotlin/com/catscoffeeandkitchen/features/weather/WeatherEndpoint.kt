package com.catscoffeeandkitchen.features.weather

import io.ktor.resources.*

@Resource("/weather")
class WeatherEndpoint {

    @Resource("current")
    class Current(val parent: WeatherEndpoint, val location: String?)

    @Resource("forecast")
    class Forecast(val parent: WeatherEndpoint, val location: String?, val days: Int = 3)
}