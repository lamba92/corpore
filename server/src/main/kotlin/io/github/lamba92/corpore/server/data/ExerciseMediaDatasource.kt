package io.github.lamba92.corpore.server.data

import io.github.lamba92.corpore.common.core.MediaResource
import kotlinx.io.Source
import okio.BufferedSink

interface ExerciseMediaDatasource {
    suspend fun getMedia(exerciseId: String): List<MediaResource>?
    suspend fun addMedia(exerciseId: String, media: MediaResource): Boolean
    suspend fun removeMedia(exerciseId: String, mediaId: String): Boolean

    suspend fun uploadMedia(name: String, session: suspend (BufferedSink) -> Unit)
    suspend fun downloadMedia(name: String): Source
}