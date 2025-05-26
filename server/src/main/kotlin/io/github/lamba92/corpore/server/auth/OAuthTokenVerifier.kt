package io.github.lamba92.corpore.server.auth

import com.appstractive.jwt.JWT
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable

interface OAuthTokenVerifier {
    suspend fun verify(token: JWT): Result

    @Serializable
    sealed interface Result {

        @Serializable
        data class Success(
            val email: String,
            val name: String,
            val pictureUrl: String? = null
        ) : Result

        @Serializable
        data class Failure(
            val reason: String? = null
        ) : Result
    }
}

class GoogleTokenVerifier(
    private val httpClient: HttpClient
) : OAuthTokenVerifier {
    override suspend fun verify(token: String): OAuthTokenVerifier.Result {
        TODO()
    }
}

class AppleTokenVerifier(
    private val httpClient: HttpClient
) : OAuthTokenVerifier {
    override suspend fun verify(token: String): OAuthTokenVerifier.Result {
        TODO()
    }
}