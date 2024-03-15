package com.catscoffeeandkitchen.plugins

import com.catscoffeeandkitchen.bongoapi.features.common.ApiKeyPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*

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
