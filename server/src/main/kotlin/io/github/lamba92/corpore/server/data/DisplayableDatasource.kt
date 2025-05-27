package io.github.lamba92.corpore.server.data

import io.github.lamba92.corpore.common.core.data.ExerciseDisplayables

interface DisplayableDatasource {
    suspend fun getAllDisplayables(exerciseId: String): Map<String, ExerciseDisplayables>

    suspend fun getDisplayables(
        exerciseId: String,
        locale: String,
    ): ExerciseDisplayables?

    suspend fun addDisplayables(displayables: ExerciseDisplayables): Boolean

    suspend fun removeDisplayables(
        exerciseId: String,
        locale: String,
    ): Boolean
}
