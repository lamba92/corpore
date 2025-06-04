package io.github.lamba92.corpore.server

import com.appstractive.jwt.JWT
import io.github.lamba92.corpore.server.di.DI
import io.github.lamba92.corpore.server.routing.authentication
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import io.ktor.server.sse.SSE
import kotlinx.serialization.json.booleanOrNull
import org.koin.ktor.plugin.Koin
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation
import org.koin.ktor.ext.get as koinGet

fun main() {
    launchEmbeddedServer(Netty, port = 8080) {
        install(Koin) {
            modules(DI.all)
        }
        install(ServerContentNegotiation) {
            json(this@launchEmbeddedServer.koinGet())
        }
        install(SSE)
        install(Authentication) {
            bearer {
                authenticate { bearerTokenCredential ->
                    JWT
                        .fromOrNull(bearerTokenCredential.token)
                        ?.takeIf {
                            it
                                .claims["isRefresh"]
                                ?.jsonPrimitiveOrNull
                                ?.booleanOrNull == false
                        }
                }
            }
        }

        routing {
            authentication()
        }
    }
}
