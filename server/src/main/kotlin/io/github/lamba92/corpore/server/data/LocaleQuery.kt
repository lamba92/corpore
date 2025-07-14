package io.github.lamba92.corpore.server.data

import io.github.lamba92.corpore.common.core.data.Locale
import kotlinx.serialization.Serializable

@Serializable
sealed interface LocaleQuery {
    @Serializable
    data class ByLanguage(val locales: List<Locale>) : LocaleQuery {
        companion object {
            val ENGLISH_ONLY = ByLanguage(listOf(Locale.Companion.ENGLISH))
        }
    }

    @Serializable
    object All : LocaleQuery
}
