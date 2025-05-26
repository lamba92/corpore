package io.github.lamba92.corpore.server

import com.appstractive.jwt.JWT
import io.github.lamba92.corpore.server.auth.GoogleTokenVerifier
import io.github.lamba92.corpore.server.auth.OAuthTokenVerifier
import io.github.lamba92.corpore.server.data.TokenRepository
import io.github.lamba92.corpore.server.data.UserRepository
import io.github.lamba92.corpore.server.di.DI
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.netty.Netty
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.sse.SSE
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import usecase.FunctionalUseCase
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation

fun main() {
    launchEmbeddedServer(Netty, port = 8080) {
        install(Koin) {
            modules(DI.all)
        }
        install(ServerContentNegotiation) {
            json(this@launchEmbeddedServer.get())
        }
        install(SSE)
        install(Authentication) {

        }

        routing {
            route("auth") {
                val userRepository: UserRepository by inject()
                val tokenRepository: TokenRepository by inject()

                get("google") {
                    val verifier: GoogleTokenVerifier by inject()
                    val googleToken = call.request.queryParameters["token"]
                    if (googleToken.isNullOrEmpty()) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }

                    val user = when (val result = verifier.verify(googleToken)) {
                        is OAuthTokenVerifier.Result.Failure -> {
                            call.respond(
                                status = HttpStatusCode.Unauthorized,
                                message = ApiResponse.Error(
                                    message = result.reason ?: "Invalid token",
                                    code = HttpStatusCode.Unauthorized.value
                                )
                            )
                            return@get
                        }

                        is OAuthTokenVerifier.Result.Success ->
                            userRepository.getOrCreateUser(
                                email = result.email,
                                name = result.name,
                                pictureUrl = result.pictureUrl
                            )
                    }

                    when (val token = tokenRepository.createToken(user.id)) {
                        is TokenRepository.CreateTokenResult.Failure -> TODO()
                        is TokenRepository.CreateTokenResult.Success -> TODO()
                    }
                }
                route("apple") {

                }
            }
        }
    }
}

class RegisterOrLoginUserUseCase(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val verifier: OAuthTokenVerifier
) : FunctionalUseCase<ApiResponse, JWT> {

    override suspend fun execute(param: JWT): ApiResponse {
        return when (val result = verifier.verify(param)) {
            is OAuthTokenVerifier.Result.Failure -> ApiResponse.Error(
                message = result.reason ?: "Invalid token",
                code = HttpStatusCode.Unauthorized.value
            )
            is OAuthTokenVerifier.Result.Success -> {
                val user = userRepository.getOrCreateUser(
                    email = result.email,
                    name = result.name,
                    pictureUrl = result.pictureUrl
                )
                when (val tokenResult = tokenRepository.createToken(user.id)) {
                    is TokenRepository.CreateTokenResult.Failure -> ApiResponse.Error(
                        message = "Failed to create token",
                        code = HttpStatusCode.InternalServerError.value
                    )
                    is TokenRepository.CreateTokenResult.Success -> ApiResponse.Success(tokenResult.token)
                }
            }
        }
    }
}


@Serializable
sealed interface ApiResponse {

    @Serializable
    data class Success<T>(
        val data: T
    ) : ApiResponse

    @Serializable
    data class Error(
        val message: String,
        val code: Int? = null,
        val stackTrace: List<String> = emptyList()
    ) : ApiResponse
}