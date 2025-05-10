@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.github.lamba92.corpore.app.core.repository

import java.util.logging.Logger

actual data object StaticLoggingRepository : LoggingRepository {
    private val logger = Logger.getLogger("PlatformLoggingRepository")

    actual override fun logEvent(
        tag: String,
        event: String,
    ) {
        logger.info("[$tag] $event")
    }

    actual override fun logError(
        tag: String,
        message: String,
    ) {
        logger.severe("[$tag] $message")
    }

    actual override fun logInfo(
        tag: String,
        message: String,
    ) {
        logger.info("[$tag] $message")
    }

    actual override fun logDebug(
        tag: String,
        message: String,
    ) {
        logger.fine("[$tag] $message")
    }

    actual override fun logWarning(
        tag: String,
        message: String,
    ) {
        logger.warning("[$tag] $message")
    }
}
