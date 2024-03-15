package com.catscoffeeandkitchen.features.jokes

import io.ktor.resources.*

@Resource("/jokes")
class JokeEndpoint {
    @Resource("random")
    class Random(val parent: JokeEndpoint = JokeEndpoint())

    @Resource("{category}")
    class Category(val parent: JokeEndpoint = JokeEndpoint(), val category: String)
}