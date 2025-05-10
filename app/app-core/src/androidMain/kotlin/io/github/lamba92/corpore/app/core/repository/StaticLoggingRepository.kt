@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.github.lamba92.corpore.app.core.repository

import android.util.Log

actual data object StaticLoggingRepository : LoggingRepository {
    actual override fun logEvent(
        tag: String,
        event: String,
    ) {
        Log.d(tag, event)
    }

    actual override fun logError(
        tag: String,
        message: String,
    ) {
        Log.e(tag, message)
    }

    actual override fun logInfo(
        tag: String,
        message: String,
    ) {
        Log.i(tag, message)
    }

    actual override fun logDebug(
        tag: String,
        message: String,
    ) {
        Log.d(tag, message)
    }

    actual override fun logWarning(
        tag: String,
        message: String,
    ) {
        Log.w(tag, message)
    }
}
