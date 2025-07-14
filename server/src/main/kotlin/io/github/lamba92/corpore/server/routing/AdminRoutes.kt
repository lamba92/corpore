package io.github.lamba92.corpore.server.routing

import io.github.lamba92.corpore.common.core.data.User
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

fun Routing.adminRoutes() {
}

fun Routing.userRoutes() {
    authenticate {
        get("me") {
            call.respond(call.principal<User>() ?: error("No principal available!"))
        }
    }
}
