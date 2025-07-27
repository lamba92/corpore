package io.github.lamba92.corpore.common.core.data

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Locale(
    val languageCode: String,
) {
    companion object {
        val ENGLISH = Locale("en")
        val FRENCH = Locale("fr")
        val GERMAN = Locale("de")
        val ITALIAN = Locale("it")
        val SPANISH = Locale("es")
        val PORTUGUESE = Locale("pt")
    }
}
