package io.github.lamba92.corpore.app.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppMetrics(
    val outerPadding: Dp,
) {
    companion object {
        val Default =
            AppMetrics(
                outerPadding = 24.dp,
            )
    }
}

val LocalAppMetrics =
    staticCompositionLocalOf<AppMetrics> { error("No AppMetrics provided") }
