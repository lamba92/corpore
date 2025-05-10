@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.github.lamba92.corpore.app.core.repository

interface LoggingRepository {
    fun logEvent(
        tag: String,
        event: String,
    )

    fun logError(
        tag: String,
        message: String,
    )

    fun logInfo(
        tag: String,
        message: String,
    )

    fun logDebug(
        tag: String,
        message: String,
    )

    fun logWarning(
        tag: String,
        message: String,
    )
}

fun LoggingRepository.logError(
    tag: String,
    exception: Throwable,
) {
    logError(tag, exception.stackTraceToString())
}

expect object StaticLoggingRepository : LoggingRepository {
    override fun logEvent(
        tag: String,
        event: String,
    )

    override fun logError(
        tag: String,
        message: String,
    )

    override fun logInfo(
        tag: String,
        message: String,
    )

    override fun logDebug(
        tag: String,
        message: String,
    )

    override fun logWarning(
        tag: String,
        message: String,
    )
}
