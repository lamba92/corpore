@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.github.lamba92.corpore.app.core.repository

import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.ConsoleHandler
import java.util.logging.Formatter
import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.Logger

actual data class StaticLoggingRepository actual constructor(override val tag: String) : LoggingRepository {
    private val logger = formattedLogger(tag)

    actual override fun logEvent(event: String) {
        logger.info(event)
    }

    actual override fun logError(message: String) {
        logger.severe(message)
    }

    actual override fun logInfo(message: String) {
        logger.info(message)
    }

    actual override fun logDebug(message: String) {
        logger.fine(message)
    }

    actual override fun logWarning(message: String) {
        logger.warning(message)
    }
}

data object CustomFormatter : Formatter() {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy - HH:mm:ss")

    override fun format(record: LogRecord): String {
        val timestamp = dateFormat.format(Date(record.millis))
        val level = record.level.name.padEnd(5)
        val loggerName = record.loggerName
        val message = formatMessage(record)
        return "[$timestamp] $level $loggerName $message\n"
    }
}

private fun formattedLogger(name: String): Logger {
    val logger = Logger.getLogger(name)
    logger.useParentHandlers = false
    val handler = ConsoleHandler()
    handler.formatter = CustomFormatter
    logger.addHandler(handler)
    logger.level = Level.ALL
    return logger
}
