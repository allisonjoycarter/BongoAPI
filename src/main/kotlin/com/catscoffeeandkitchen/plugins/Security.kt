package com.catscoffeeandkitchen.plugins

import com.catscoffeeandkitchen.features.common.ApiKeyPrincipal
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication

fun Application.configureSecurity() {
    val allowedAPI = environment.config.propertyOrNull("api.allowed")?.getString()

    install(Authentication) {
        apiKey("auth-api-key") {
            validate { keyFromHeader ->
                keyFromHeader
                    .takeIf { allowedAPI != null && it == allowedAPI }
                    ?.let { ApiKeyPrincipal(it) }
            }
        }
    }
}
