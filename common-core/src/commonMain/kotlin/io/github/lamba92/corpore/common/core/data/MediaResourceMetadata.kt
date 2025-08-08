package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable

@Serializable
data class MediaResourceMetadata(
    val type: MediaType,
    val url: String,
)
