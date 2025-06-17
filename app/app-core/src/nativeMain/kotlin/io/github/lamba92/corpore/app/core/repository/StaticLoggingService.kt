@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.github.lamba92.corpore.app.core.repository

import kotlinx.datetime.Clock

actual class StaticLoggingService actual constructor(
    actual override val tag: String,
) : LoggingService {
    private val now
        get() = Clock.System.now()

    actual override fun logEvent(event: String) {
        println("$now | EVENT   [$tag] $event")
    }

    actual override fun logError(message: String) {
        println("$now | ERROR   [$tag] $message")
    }

    actual override fun logInfo(message: String) {
        println("$now | INFO    [$tag] $message")
    }

    actual override fun logDebug(message: String) {
        println("$now | DEBUG   [$tag] $message")
    }

    actual override fun logWarning(message: String) {
        println("$now | WARNING [$tag] $message")
    }
}
