package com.catscoffeeandkitchen.features.namegenerator

import io.ktor.resources.Resource

@Resource("/name-generator")
class NameGeneratorEndpoint {

    @Resource("{category}")
    class Category(
        val parent: NameGeneratorEndpoint,
        val category: String,
        val amount: Int = 1,
        val context: String? = null
    )
}
