package io.github.lamba92.corpore.server.usecase

import com.appstractive.jwt.JWT
import io.github.lamba92.corpore.common.core.usecase.FunctionalUseCase
import io.github.lamba92.corpore.server.auth.OAuthTokenVerifier
import io.github.lamba92.corpore.server.data.SerializableJWT
import io.github.lamba92.corpore.server.data.TokenRepository
import io.github.lamba92.corpore.server.data.UserRepository
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable

abstract class RegisterOrLoginUserUseCase(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val verifier: OAuthTokenVerifier,
) : FunctionalUseCase<JWT, RegisterOrLoginUserUseCase.Result> {
    @Serializable
    sealed interface Result {
        @Serializable
        data class Success(
            val accessToken: SerializableJWT,
            val refreshToken: SerializableJWT,
        ) : Result

        @Serializable
        data class Failure(val message: String, val code: Int) : Result
    }

    override suspend fun execute(param: JWT): Result {
        return when (val result = verifier.verify(param)) {
            is OAuthTokenVerifier.Result.Failure ->
                Result.Failure(
                    message = result.reason ?: "Invalid token",
                    code = HttpStatusCode.Unauthorized.value,
                )

            is OAuthTokenVerifier.Result.Success -> {
                val user =
                    userRepository.getOrCreateUser(
                        email = result.email,
                        name = result.name,
                        pictureUrl = result.pictureUrl,
                    )
                when (val tokenResult = tokenRepository.createToken(user.id)) {
                    is TokenRepository.CreateTokenResult.Failure ->
                        Result.Failure(
                            message = "Failed to create token",
                            code = HttpStatusCode.InternalServerError.value,
                        )

                    is TokenRepository.CreateTokenResult.Success ->
                        Result.Success(
                            accessToken = tokenResult.accessToken,
                            refreshToken = tokenResult.refreshToken,
                        )
                }
            }
        }
    }
}
