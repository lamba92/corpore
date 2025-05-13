@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.github.lamba92.corpore.app.core.repository

interface LoggingRepository {
    val tag: String

    fun logEvent(event: String)

    fun logError(message: String)

    fun logInfo(message: String)

    fun logDebug(message: String)

    fun logWarning(message: String)
}

fun LoggingRepository.logError(exception: Throwable) {
    logError(exception.stackTraceToString())
}

expect class StaticLoggingRepository(tag: String) : LoggingRepository {
    override val tag: String

    override fun logEvent(event: String)

    override fun logError(message: String)

    override fun logInfo(message: String)

    override fun logDebug(message: String)

    override fun logWarning(message: String)
}
