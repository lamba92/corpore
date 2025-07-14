package io.github.lamba92.corpore.server.di

import io.github.lamba92.corpore.server.auth.AppleTokenVerifier
import io.github.lamba92.corpore.server.auth.GoogleTokenVerifier
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

object DI {
    val core =
        module {
            single {
                Json {
                    ignoreUnknownKeys = true
                    isLenient = false
                    encodeDefaults = true
                    explicitNulls = false
                }
            }
            single {
                HttpClient(Apache) {
                    install(ContentNegotiation) {
                        json(json = get())
                    }
                }
            }
        }

    val auth =
        module {
            single { GoogleTokenVerifier(get()) }
            single { AppleTokenVerifier(get()) }
        }

    val allModules = core + auth
}
