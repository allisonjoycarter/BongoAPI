package com.catscoffeeandkitchen.features.namegenerator

import io.ktor.resources.Resource
import kotlinx.serialization.EncodeDefault

@Resource("/name-generator")
class NameGeneratorEndpoint {

    @Resource("{category}")
    class Category(
        val parent: NameGeneratorEndpoint,
        val category: String,
        @EncodeDefault val amount: Int = 1,
        val context: String? = null
    )
}
