package io.github.lamba92.corpore.server

import io.ktor.server.application.install
import io.ktor.server.netty.Netty
import org.koin.ktor.plugin.Koin

suspend fun main() {
    launchEmbeddedServer(Netty, port = 8080) {
        install(Koin) {
            printLogger()
        }
    }
}
