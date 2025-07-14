package io.github.lamba92.corpore.server.routing

import com.appstractive.jwt.JWT
import com.appstractive.jwt.from
import io.github.lamba92.corpore.common.core.http.request.LoginRequest
import io.github.lamba92.corpore.common.core.http.request.RefreshTokenRequest
import io.github.lamba92.corpore.common.core.http.response.ApiResponse
import io.github.lamba92.corpore.server.auth.TokenRefresher
import io.github.lamba92.corpore.server.fromOrNull
import io.github.lamba92.corpore.server.usecase.RegisterOrLoginUserUseCase
import io.github.lamba92.corpore.server.usecase.RegisterOrLoginUserWithAppleUseCase
import io.github.lamba92.corpore.server.usecase.RegisterOrLoginUserWithGoogleUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Routing.authentication() {
    route("auth") {
        post("google") {
            val verifier: RegisterOrLoginUserWithGoogleUseCase by inject()
            loginWithToken(verifier)
        }
        post("apple") {
            val verifier: RegisterOrLoginUserWithAppleUseCase by inject()
            loginWithToken(verifier)
        }
        post("refresh") {
            val refresher: TokenRefresher by inject()
            refreshToken(refresher)
        }
    }
}

private suspend fun RoutingContext.refreshToken(refresher: TokenRefresher) {
    val refreshToken =
        call
            .request
            .queryParameters["refreshToken"]
            ?.let { JWT.fromOrNull(it) }
            ?: call.receiveNullable<RefreshTokenRequest>()
                ?.token
                ?.let { JWT.fromOrNull(it) }

    if (refreshToken == null) {
        call.respond(
            ApiResponse(
                errorMessage = "Refresh token is required",
                errorCode = HttpStatusCode.BadRequest.value,
            ),
        )
        return
    }
    when (val result = refresher.refresh(refreshToken)) {
        is TokenRefresher.Result.Failure ->
            call.respond(
                ApiResponse(
                    errorMessage = result.reason ?: "Failed to refresh token",
                    errorCode = HttpStatusCode.InternalServerError.value,
                ),
            )

        is TokenRefresher.Result.Success ->
            call.respond(ApiResponse(accessToken = result.token.toString()))
    }
}

private suspend fun RoutingContext.loginWithToken(verifier: RegisterOrLoginUserUseCase) {
    val token =
        call.parameters["token"]
            ?: call.receiveNullable<LoginRequest>()?.oauthToken

    if (token == null) {
        call.respond(
            status = HttpStatusCode.BadRequest,
            message =
                ApiResponse(
                    errorMessage = "Token is required",
                    errorCode = HttpStatusCode.BadRequest.value,
                ),
        )
        return
    }
    when (val result = verifier.execute(JWT.from(token))) {
        is RegisterOrLoginUserUseCase.Result.Failure ->
            call.respond(
                status = HttpStatusCode.fromValue(result.code),
                message =
                    ApiResponse(
                        errorMessage = result.message,
                        errorCode = result.code,
                    ),
            )

        is RegisterOrLoginUserUseCase.Result.Success ->
            call.respond(
                ApiResponse(
                    accessToken = result.accessToken.toString(),
                    refreshToken = result.refreshToken.toString(),
                ),
            )
    }
}
