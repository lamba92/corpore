package io.github.lamba92.corpore.app.core.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.baseline_arrow_back_24
import io.github.lamba92.app_core.generated.resources.baseline_logout_24
import io.github.lamba92.app_core.generated.resources.onboarding_back
import io.github.lamba92.app_core.generated.resources.onboarding_create_plan
import io.github.lamba92.app_core.generated.resources.onboarding_next
import io.github.lamba92.app_core.generated.resources.outline_arrow_forward_24
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    buttonColors: ButtonColors,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = buttonColors,
        content = content,
    )
}

@Composable
fun OnboardingNextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
) {
    OnboardingButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        buttonColors =
            ButtonDefaults.buttonColors(
                containerColor = CorporeTheme.colorScheme.primary,
                contentColor = CorporeTheme.colorScheme.onPrimary,
            ),
    ) {
        Text(
            text = stringResource(Res.string.onboarding_next),
        )
        Image(
            painter = painterResource(Res.drawable.outline_arrow_forward_24),
            contentDescription = "Arrow forward icon",
        )
    }
}

@Composable
fun OnboardingGeneratePlanButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
) {
    OnboardingButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        buttonColors =
            ButtonDefaults.filledTonalButtonColors(
                containerColor = CorporeTheme.colorScheme.primary,
                contentColor = CorporeTheme.colorScheme.onPrimary,
            ),
    ) {
        Text(stringResource(Res.string.onboarding_create_plan))
    }
}

@Composable
fun OnboardingBackButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    isLastScreen: Boolean,
) {
    OnboardingButton(
        onClick = onClick,
        modifier = modifier,
        buttonColors =
            ButtonDefaults.filledTonalButtonColors(
                containerColor = CorporeTheme.colorScheme.secondary,
                contentColor = CorporeTheme.colorScheme.onSecondary,
            ),
        enabled = enabled,
    ) {
        Image(
            painter = painterResource(Res.drawable.baseline_arrow_back_24),
            contentDescription = "Arrow back icon",
        )
        if (!isLastScreen) {
            Text(
                text = stringResource(Res.string.onboarding_back),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun LogoutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(Res.drawable.baseline_logout_24),
            contentDescription = "Logout icon",
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}
