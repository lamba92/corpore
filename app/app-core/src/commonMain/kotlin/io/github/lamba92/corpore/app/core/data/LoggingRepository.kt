package io.github.lamba92.corpore.app.core.data

interface LoggingRepository {
    fun logEvent(event: String)

    fun logError(message: String)

    fun logInfo(message: String)

    fun logDebug(message: String)

    fun logWarning(message: String)
}

fun LoggingRepository.logError(exception: Throwable) {
    logError(exception.stackTraceToString())
}
