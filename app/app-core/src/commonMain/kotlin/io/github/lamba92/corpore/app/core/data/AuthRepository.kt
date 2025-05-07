package io.github.lamba92.corpore.app.core.data

import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

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
