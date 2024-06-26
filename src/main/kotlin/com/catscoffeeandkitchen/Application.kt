package com.catscoffeeandkitchen

import com.catscoffeeandkitchen.di.appModule
import com.catscoffeeandkitchen.plugins.configureHTTP
import com.catscoffeeandkitchen.plugins.configureMonitoring
import com.catscoffeeandkitchen.plugins.configureRouting
import com.catscoffeeandkitchen.plugins.configureSecurity
import com.catscoffeeandkitchen.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
