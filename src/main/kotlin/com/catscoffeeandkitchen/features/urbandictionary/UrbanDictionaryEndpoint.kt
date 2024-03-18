package com.catscoffeeandkitchen.features.urbandictionary

import io.ktor.resources.Resource

@Resource("/urban")
class UrbanDictionaryEndpoint {

    @Resource("random")
    class Random(val parent: UrbanDictionaryEndpoint)

    @Resource("define")
    class Define(val parent: UrbanDictionaryEndpoint, val term: String)
}
