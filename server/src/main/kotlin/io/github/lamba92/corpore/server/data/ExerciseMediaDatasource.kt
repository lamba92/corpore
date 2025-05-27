package io.github.lamba92.corpore.server.data

import io.github.lamba92.corpore.common.core.data.MediaResource
import kotlinx.io.Sink
import kotlinx.io.Source

interface ExerciseMediaDatasource {
    suspend fun getMedia(exerciseId: String): List<MediaResource>?

    suspend fun addMedia(
        exerciseId: String,
        media: MediaResource,
    ): Boolean

    suspend fun removeMedia(
        exerciseId: String,
        mediaId: String,
    ): Boolean

    suspend fun uploadMedia(
        name: String,
        session: suspend (Sink) -> Unit,
    )

    suspend fun downloadMedia(name: String): Source
}
