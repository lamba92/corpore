@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.github.lamba92.corpore.app.core.repository

import android.util.Log

actual data class StaticLoggingRepository actual constructor(
    actual override val tag: String,
) : LoggingRepository {
    actual override fun logEvent(event: String) {
        Log.d(tag, event)
    }

    actual override fun logError(message: String) {
        Log.e(tag, message)
    }

    actual override fun logInfo(message: String) {
        Log.i(tag, message)
    }

    actual override fun logDebug(message: String) {
        Log.d(tag, message)
    }

    actual override fun logWarning(message: String) {
        Log.w(tag, message)
    }
}
