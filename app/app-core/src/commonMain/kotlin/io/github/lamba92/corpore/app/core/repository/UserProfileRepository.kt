@file:OptIn(ExperimentalTime::class)

package io.github.lamba92.corpore.app.core.repository

import io.github.lamba92.corpore.common.core.data.EpochMillisSerializableInstant
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class UserProfile(
    val name: String,
    val email: String,
    val yearOfBirth: Int,
    val registeredAt: EpochMillisSerializableInstant,
    val profilePictureUrl: String? = null,
)

interface UserProfileRepository {
    val userProfile: StateFlow<UserProfile?>

    suspend fun updateUserProfile(userProfile: UserProfile)

    suspend fun deleteUserProfile()
}
