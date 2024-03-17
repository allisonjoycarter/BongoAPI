package com.catscoffeeandkitchen.features.poe

import io.ktor.resources.Resource

@Resource("/poe")
class PoeEndpoint {
    @Resource("prices")
    class Prices(val parent: PoeEndpoint = PoeEndpoint()) {
        @Resource("bulk")
        class Bulk(val parent: Prices = Prices(), val query: String)

        @Resource("search")
        class Search(val parent: Prices = Prices(), val query: String)

        @Resource("chaos")
        class ChaosEquivalent(val parent: Prices = Prices(), val query: String = "Divine Orb")

         @Resource("item")
         class Item(val parent: Prices = Prices())
    }
}
