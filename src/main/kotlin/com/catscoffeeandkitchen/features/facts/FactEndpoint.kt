package com.catscoffeeandkitchen.features.facts

import io.ktor.resources.*

@Resource("/facts")
class FactEndpoint {

    @Resource("random")
    class Random(val parent: FactEndpoint)
}