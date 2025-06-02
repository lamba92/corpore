package io.github.lamba92.corpore.server

import com.appstractive.jwt.JWT
import com.appstractive.jwt.from
import io.ktor.server.application.Application
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

fun <TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration> launchEmbeddedServer(
    factory: ApplicationEngineFactory<TEngine, TConfiguration>,
    port: Int = 80,
    host: String = "0.0.0.0",
    watchPaths: List<String> = listOf(SystemFileSystem.resolve(Path(".")).toString()),
    module: Application.() -> Unit,
): EmbeddedServer<TEngine, TConfiguration> {
    val server =
        embeddedServer(factory, port, host, watchPaths, module)
    server.start(wait = false)
    return server
}

val JsonElement.jsonPrimitiveOrNull
    get() = this as? JsonPrimitive

fun JWT.Companion.fromOrNull(jwt: String) = runCatching { from(jwt) }.getOrNull()
