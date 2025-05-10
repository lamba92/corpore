package io.github.lamba92.corpore.app.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// this class might not be a good idea...
data class AppMetrics(
    val outerPadding: Dp,
    val innerPadding: Dp,
) {
    companion object {
        val Default =
            AppMetrics(
                outerPadding = 16.dp,
                innerPadding = 24.dp,
            )
    }
}

val LocalAppMetrics =
    staticCompositionLocalOf<AppMetrics> { error("No AppMetrics provided") }
