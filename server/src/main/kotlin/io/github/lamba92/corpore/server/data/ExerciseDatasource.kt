package io.github.lamba92.corpore.server.data

import io.github.lamba92.corpore.common.core.data.Exercise
import kotlinx.serialization.Serializable

interface ExerciseDatasource {
    suspend fun listAll(
        page: Int = 0,
        size: Int = 25,
    ): Page<Exercise.WithId>

    suspend fun search(
        query: String,
        page: Int = 0,
        size: Int = 25,
    ): Page<Exercise.WithId>

    suspend fun getById(id: String): Exercise.WithId?

    suspend fun addExercise(exercise: Exercise.WithoutId): String

    suspend fun markAsVerified(exerciseId: String): Boolean

    @Serializable
    data class Page<T>(
        val page: Int,
        val size: Int,
        val total: Int,
        val items: List<T>,
    )
}
