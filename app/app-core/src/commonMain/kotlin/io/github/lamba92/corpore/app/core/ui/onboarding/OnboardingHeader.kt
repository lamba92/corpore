package io.github.lamba92.corpore.app.core.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.app_name
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingHeader(
    pageNumber: Int,
    totalPages: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Text(
            text = stringResource(Res.string.app_name),
            style = CorporeTheme.typography.titleLarge,
            color = CorporeTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterStart),
        )
        Text(
            text = "$pageNumber/$totalPages",
            style = CorporeTheme.typography.bodySmall,
            color = CorporeTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}
