package io.github.lamba92.corpore.app.core.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.ui.theme.appMetrics

@Composable
fun VerticalSpacer(height: Dp = CorporeTheme.appMetrics.outerPadding) {
    Spacer(modifier = Modifier.padding(height))
}

@Composable
fun HorizontalSpacer(width: Dp = CorporeTheme.appMetrics.outerPadding) {
    Spacer(modifier = Modifier.padding(width))
}
