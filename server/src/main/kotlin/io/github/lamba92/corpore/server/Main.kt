package io.github.lamba92.corpore.server

import io.github.lamba92.corpore.common.core.Exercise
import io.github.lamba92.corpore.common.core.ExerciseDisplayables
import io.github.lamba92.corpore.common.core.MediaResource
import io.ktor.server.application.install
import io.ktor.server.netty.Netty
import io.ktor.server.sse.SSE
import kotlinx.io.Source
import kotlinx.serialization.Serializable
import okio.BufferedSink
import org.koin.ktor.plugin.Koin

fun main() {
    launchEmbeddedServer(Netty, port = 8080) {
        install(Koin) {
            printLogger()
        }
        install(SSE)
    }
}

interface ExerciseMediaDatasource {
    suspend fun getMedia(exerciseId: String): List<MediaResource>?
    suspend fun addMedia(exerciseId: String, media: MediaResource): Boolean
    suspend fun removeMedia(exerciseId: String, mediaId: String): Boolean

    suspend fun uploadMedia(name: String, session: suspend (BufferedSink) -> Unit)
    suspend fun downloadMedia(name: String): Source
}

interface DisplayableDatasource {
    suspend fun getAllDisplayables(exerciseId: String): Map<String, ExerciseDisplayables>
    suspend fun getDisplayables(exerciseId: String, locale: String): ExerciseDisplayables?
    suspend fun addDisplayables(displayables: ExerciseDisplayables): Boolean
    suspend fun removeDisplayables(exerciseId: String, locale: String): Boolean
}

interface ExerciseDatasource {
    suspend fun listAll(page: Int = 0, size: Int = 25): Page<Exercise.WithId>
    suspend fun search(query: String, page: Int = 0, size: Int = 25): Page<Exercise.WithId>
    suspend fun getById(id: String): Exercise.WithId?
    suspend fun addExercise(exercise: Exercise.WithoutId): String
    suspend fun markAsVerified(exerciseId: String): Boolean

    @Serializable
    data class Page<T>(
        val page: Int,
        val size: Int,
        val total: Int,
        val items: List<T>
    )
}
