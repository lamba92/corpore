package io.github.lamba92.corpore.common.core

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseWithMetrics(
    val exercise: Exercise.WithId,
    val metrics: ExerciseMetrics,
)