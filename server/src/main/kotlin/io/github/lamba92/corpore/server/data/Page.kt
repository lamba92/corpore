package io.github.lamba92.corpore.server.data

import kotlinx.serialization.Serializable

@Serializable
data class Page<T>(
    val page: Int,
    val pageSize: Int,
    val totalItems: Int,
    val items: List<T>,
)
