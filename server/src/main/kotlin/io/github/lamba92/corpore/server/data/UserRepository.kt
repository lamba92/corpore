package io.github.lamba92.corpore.server.data

import io.github.lamba92.corpore.common.core.UserId
import io.github.lamba92.corpore.common.core.data.User
import io.github.lamba92.corpore.common.core.data.WorkoutPlan
import io.github.lamba92.corpore.common.core.data.WorkoutSession

interface UserRepository {
    suspend fun getOrCreateUser(
        email: String,
        name: String? = null,
        pictureUrl: String? = null,
    ): User

    suspend fun getUserById(id: UserId): User?

    suspend fun isAdministrator(id: UserId): Boolean
}

interface WorkoutRepository {
    suspend fun getWorkoutPlanForUser(
        id: UserId,
        localeQuery: LocaleQuery = LocaleQuery.ByLanguage.ENGLISH_ONLY,
    ): WorkoutPlan?

    suspend fun getCurrentWorkoutSessionForUser(
        id: UserId,
        localeQuery: LocaleQuery = LocaleQuery.ByLanguage.ENGLISH_ONLY,
    ): WorkoutSession?

    suspend fun getAllWorkoutSessionsForUser(
        id: UserId,
        localeQuery: LocaleQuery = LocaleQuery.ByLanguage.ENGLISH_ONLY,
        page: Int = 0,
        size: Int = 25,
    ): Page<WorkoutSession>

    suspend fun updateCurrentWorkoutSessionForUser(
        id: UserId,
        workoutSession: WorkoutSession,
    )

    suspend fun updateWorkoutPlanForUser(
        id: UserId,
        workoutPlan: WorkoutPlan,
    )
}
