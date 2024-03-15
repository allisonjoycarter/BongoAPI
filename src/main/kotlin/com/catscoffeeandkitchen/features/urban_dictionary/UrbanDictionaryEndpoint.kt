package com.catscoffeeandkitchen.features.urban_dictionary

import io.ktor.resources.*

@Resource("/urban")
class UrbanDictionaryEndpoint {

    @Resource("random")
    class Random(val parent: UrbanDictionaryEndpoint)

    @Resource("define/{term}")
    class Define(val parent: UrbanDictionaryEndpoint, val term: String)
}