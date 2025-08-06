package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Serializable
data class WorkoutTimings(
    val timings: Timings = Timings.EMPTY,
    val totalTime: TimeInterval = TimeInterval.NotStarted,
) {
    companion object {
        val NONE = WorkoutTimings()
    }

    @Serializable
    data class Timings(
        val warmup: List<TimeInterval> = emptyList(),
        val exercises: List<TimeInterval> = emptyList(),
        val cooldown: List<TimeInterval> = emptyList(),
    ) {
        companion object {
            val EMPTY = Timings()
        }
    }
}

@OptIn(ExperimentalTime::class)
@Serializable
sealed interface TimeInterval {
    @Serializable
    @SerialName("not_started")
    data object NotStarted : TimeInterval

    @Serializable
    @SerialName("ongoing")
    data class Ongoing(
        val startTime: EpochMillisSerializableInstant,
    ) : TimeInterval

    @Serializable
    @SerialName("finished")
    data class Finished(
        val startTime: EpochMillisSerializableInstant,
        val endTime: EpochMillisSerializableInstant,
    ) : TimeInterval
}
