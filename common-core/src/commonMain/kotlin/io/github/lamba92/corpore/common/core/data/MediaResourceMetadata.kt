package io.github.lamba92.corpore.common.core.data

import io.github.lamba92.corpore.common.core.MediaId
import kotlinx.serialization.Serializable

@Serializable
data class MediaResourceMetadata(
    val id: MediaId,
    val exerciseId: String,
    val type: MediaType,
    val url: String,
)
