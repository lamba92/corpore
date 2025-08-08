package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.units.Length
import io.github.lamba92.corpore.common.core.units.Weight
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class GymFitness(
    val hasTrainedBefore: Boolean = false,
    val benchPress1RM: Weight = Weight.ZERO,
    val squat1RM: Weight = Weight.ZERO,
    val deadlift1RM: Weight = Weight.ZERO,
)

@Serializable
data class RunningFitness(
    val hasTrainedBefore: Boolean = false,
    val distanceIn30Mins: Length = Length.ZERO,
)

@Serializable
data class SwimmingFitness(
    val hasTrainedBefore: Boolean = false,
    val freestyleDistance15Min: Length = Length.ZERO,
    val knownStrokes: Set<SwimmingStroke> = setOf(SwimmingStroke.Freestyle),
)

@Serializable
data class CalisthenicsFitness(
    val hasTrainedBefore: Boolean = false,
    val maxPushups: Int = 0,
    val wallSitHold: Duration = Duration.ZERO,
    val canPlank30Sec: Boolean = false,
)