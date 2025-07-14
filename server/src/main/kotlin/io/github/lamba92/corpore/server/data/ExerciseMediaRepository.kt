package io.github.lamba92.corpore.server.data

import io.github.lamba92.corpore.common.core.ExerciseId
import io.github.lamba92.corpore.common.core.MediaId
import io.github.lamba92.corpore.common.core.data.MediaResourceMetadata
import kotlinx.io.Sink

interface ExerciseMediaRepository {
    suspend fun getMedia(exerciseId: ExerciseId): List<MediaResourceMetadata>?

    suspend fun addMedia(
        exerciseId: ExerciseId,
        metadata: MediaResourceMetadata,
        session: suspend (Sink) -> Unit,
    ): Boolean

    suspend fun removeMedia(
        exerciseId: String,
        mediaId: MediaId,
    ): Boolean
}
