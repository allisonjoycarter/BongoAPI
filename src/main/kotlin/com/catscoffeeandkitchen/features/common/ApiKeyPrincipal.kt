package com.catscoffeeandkitchen.features.common

import io.ktor.server.auth.Principal

data class ApiKeyPrincipal(val key: String): Principal
