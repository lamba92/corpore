package io.github.lamba92.corpore.server.data

import io.github.lamba92.corpore.common.core.ExerciseId
import io.github.lamba92.corpore.common.core.data.Exercise
import io.github.lamba92.corpore.common.core.data.ExerciseDisplayable
import io.github.lamba92.corpore.common.core.data.Locale

interface ExerciseRepository {
    suspend fun listAll(
        localeQuery: LocaleQuery = LocaleQuery.ByLanguage.ENGLISH_ONLY,
        page: Int = 0,
        size: Int = 25,
    ): Page<Exercise.WithId>

    suspend fun search(
        query: String,
        localeQuery: LocaleQuery = LocaleQuery.ByLanguage.ENGLISH_ONLY,
        page: Int = 0,
        size: Int = 25,
    ): Page<Exercise.WithId>

    suspend fun getById(id: ExerciseId): Exercise.WithId?

    suspend fun addExercise(exercise: Exercise.WithoutId): String

    suspend fun markAsVerified(exerciseId: ExerciseId): Boolean

    suspend fun updateExercise(
        exercise: Exercise.WithId,
        upsert: Boolean = false,
    ): Boolean

    suspend fun updateExerciseDisplayable(
        exerciseId: ExerciseId,
        displayables: Map<Locale, ExerciseDisplayable>,
    ): Boolean
}
