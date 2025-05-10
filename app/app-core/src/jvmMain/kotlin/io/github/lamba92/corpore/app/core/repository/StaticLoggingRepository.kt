@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.github.lamba92.corpore.app.core.repository

import java.util.logging.Logger

actual data class StaticLoggingRepository actual constructor(override val tag: String) : LoggingRepository {
    private val logger = Logger.getLogger("PlatformLoggingRepository")

    actual override fun logEvent(event: String) {
        logger.info("[$tag] $event")
    }

    actual override fun logError(message: String) {
        logger.severe("[$tag] $message")
    }

    actual override fun logInfo(message: String) {
        logger.info("[$tag] $message")
    }

    actual override fun logDebug(message: String) {
        logger.fine("[$tag] $message")
    }

    actual override fun logWarning(message: String) {
        logger.warning("[$tag] $message")
    }
}
