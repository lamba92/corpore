@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.github.lamba92.corpore.app.core.repository

import coil3.util.Logger
import io.github.lamba92.corpore.app.core.utils.CoilLogger

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

object CoilDebugLogger : CoilLogger {
    private val logger = StaticLoggingRepository("CoilDebugLogger")

    override var minLevel = Logger.Level.Debug

    override fun log(
        tag: String,
        level: Logger.Level,
        message: String?,
        throwable: Throwable?,
    ) {
        when (level) {
            Logger.Level.Debug -> logger.logDebug(message ?: "")
            Logger.Level.Error -> logger.logError(message ?: "")
            Logger.Level.Info -> logger.logInfo(message ?: "")
            Logger.Level.Warn -> logger.logWarning(message ?: "")
            Logger.Level.Verbose -> {}
        }
    }
}

expect class StaticLoggingRepository(tag: String) : LoggingRepository {
    override val tag: String

    override fun logEvent(event: String)

    override fun logError(message: String)

    override fun logInfo(message: String)

    override fun logDebug(message: String)

    override fun logWarning(message: String)
}
