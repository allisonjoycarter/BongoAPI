package com.catscoffeeandkitchen.features.advice

import io.ktor.resources.*

@Resource("/advice")
class AdviceEndpoint {

    @Resource("random")
    class Random(val parent: AdviceEndpoint)
}