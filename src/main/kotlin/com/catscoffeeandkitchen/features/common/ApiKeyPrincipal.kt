package com.catscoffeeandkitchen.bongoapi.features.common

import io.ktor.server.auth.*

data class ApiKeyPrincipal(val key: String): Principal
