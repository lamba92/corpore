package io.github.lamba92.corpore.app.core.repository

import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val name: String,
    val email: String,
    val yearOfBirth: Int,
    val registeredAt: Instant,
    val profilePictureUrl: String? = null,
)

interface UserProfileRepository {
    val userProfile: StateFlow<UserProfile?>

    suspend fun updateUserProfile(userProfile: UserProfile)

    suspend fun deleteUserProfile()
}
