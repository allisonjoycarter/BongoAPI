package com.catscoffeeandkitchen.features.poe

import com.catscoffeeandkitchen.features.common.ReturnableHttpException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.receiveText
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.util.logging.error
import org.koin.ktor.ext.inject

fun Route.poeRoutes() {
    val repository by inject<PoeRepository>()

    get<PoeEndpoint.Prices.ChaosEquivalent>{ request ->
        try {
            val data = repository.getCurrencyInChaos(
                search = request.query,
                league = null
            )
            call.respond(data)
        } catch (error: ClientRequestException) {
            call.application.log.error(error)
            call.respond(HttpStatusCode.Companion.BadGateway, "Received ${error.response.status.description}")
        }
    }

    get<PoeEndpoint.Prices.Bulk>{ item ->
        try {
            val prices = repository.searchForBulkPricing(item.query)
            call.respond(prices ?: "")
        } catch (error: ReturnableHttpException) {
            call.application.log.error(error)
            call.respond(HttpStatusCode.Companion.BadGateway, error.message)
        }
    }

    get<PoeEndpoint.Prices.Search> { item ->
        try {
            val data = repository.searchForPricing(search = item.query)
            call.respond(data)
        } catch (error: ReturnableHttpException) {
            call.application.log.error(error)
            call.respond(HttpStatusCode.Companion.BadGateway, error.message)
        }
    }

    post("poe/prices/item") {
        try {
            val data = repository.searchItemPrice(call.receiveText())
            call.respond(data)
        } catch (error: ClientRequestException) {
            call.application.log.error(error)
            call.respond(HttpStatusCode.Companion.BadGateway, "Received ${error.response.status.description}")
        }
    }
}
