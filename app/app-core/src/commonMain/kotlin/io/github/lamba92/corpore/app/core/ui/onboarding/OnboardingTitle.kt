package io.github.lamba92.corpore.app.core.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.lamba92.corpore.app.core.ui.components.VerticalSpacer
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.ui.theme.appMetrics

@Composable
fun OnboardingTitle(
    title: String,
    subtitle: String,
) {
    Column {
        Text(
            text = title,
            style = CorporeTheme.typography.titleLarge,
            color = CorporeTheme.colorScheme.onBackground,
        )
        VerticalSpacer(height = CorporeTheme.appMetrics.outerPadding / 2)
        Text(
            text = subtitle,
            style = CorporeTheme.typography.titleMedium,
            color = CorporeTheme.colorScheme.onBackground,
        )
    }
}
