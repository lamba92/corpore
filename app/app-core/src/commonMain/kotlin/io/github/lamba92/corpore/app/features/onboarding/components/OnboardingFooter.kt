package io.github.lamba92.corpore.app.features.onboarding.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.ui.theme.appMetrics
import io.github.lamba92.corpore.app.features.onboarding.OnboardingEvent

@Composable
fun OnboardingFooter(
    isFirstScreen: Boolean,
    isLastScreen: Boolean,
    canGoNext: Boolean,
    onEvent: (OnboardingEvent) -> Unit,
    modifier: Modifier = Modifier,
    isLoggingOut: Boolean,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(CorporeTheme.appMetrics.outerPadding / 2),
    ) {
        val leftButtonWeight by animateFloatAsState(if (isFirstScreen || isLastScreen) 0.2f else 0.5f)
        val rightButtonWeight by derivedStateOf { 1f - leftButtonWeight }

        when {
            isFirstScreen ->
                LogoutButton(
                    enabled = !isLoggingOut,
                    onClick = { onEvent(OnboardingEvent.BackClick) },
                    modifier = Modifier.weight(leftButtonWeight),
                )

            else ->
                OnboardingBackButton(
                    isLastScreen = isLastScreen,
                    onClick = { onEvent(OnboardingEvent.BackClick) },
                    modifier = Modifier.weight(leftButtonWeight),
                )
        }
        when {
            isLastScreen ->
                OnboardingGeneratePlanButton(
                    onClick = { onEvent(OnboardingEvent.NextClick) },
                    enabled = canGoNext,
                    modifier = Modifier.weight(rightButtonWeight),
                )

            else ->
                OnboardingNextButton(
                    onClick = { onEvent(OnboardingEvent.NextClick) },
                    enabled = canGoNext,
                    modifier = Modifier.weight(rightButtonWeight),
                )
        }
    }
}
