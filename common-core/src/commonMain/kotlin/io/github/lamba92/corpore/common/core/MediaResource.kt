package io.github.lamba92.corpore.common.core

import kotlinx.serialization.Serializable

@Serializable
data class MediaResource(
    val id: MediaId,
    val exerciseId: String,
    val type: MediaType,
    val url: String,
)