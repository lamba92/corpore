package io.github.lamba92.corpore.app.core.repository

actual class StaticLoggingRepository actual constructor() : LoggingRepository {
    actual override fun logEvent(event: String) {
    }

    actual override fun logError(message: String) {
    }

    actual override fun logInfo(message: String) {
    }

    actual override fun logDebug(message: String) {
    }

    actual override fun logWarning(message: String) {
    }
}
