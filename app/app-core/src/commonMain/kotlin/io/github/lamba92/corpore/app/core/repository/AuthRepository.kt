package io.github.lamba92.corpore.app.core.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds

@Serializable
data class AuthSession(
    val uid: String,
    val email: String?,
    val idToken: String,
    val refreshToken: String? = null,
)

interface AuthRepository {
    val authSession: StateFlow<AuthSession?>

    suspend fun loginWithGoogle(): AuthResult

    suspend fun loginWithApple(): AuthResult

    suspend fun logout()
}

sealed interface AuthResult {
    data object Success : AuthResult

    data class Failure(val error: Throwable) : AuthResult
}

object MockAuthRepository : AuthRepository {
    val MockAuthSession =
        AuthSession(
            uid = "mockUid",
            email = "mockEmail",
            refreshToken = "mockRefreshToken",
            idToken = "mockIdToken",
        )

    override val authSession: MutableStateFlow<AuthSession?> = MutableStateFlow(null)

    override suspend fun loginWithGoogle(): AuthResult = login()

    override suspend fun loginWithApple(): AuthResult = login()

    private suspend fun login(): AuthResult.Success {
        delay(1.5.seconds)
        authSession.value = MockAuthSession
        return AuthResult.Success
    }

    override suspend fun logout() {
        delay(1.5.seconds)
        authSession.value = null
    }
}
