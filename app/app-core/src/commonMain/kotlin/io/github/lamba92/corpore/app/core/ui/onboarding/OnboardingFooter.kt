package io.github.lamba92.corpore.app.core.ui.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingFooter(
    isFirstScreen: Boolean,
    isLastScreen: Boolean,
    canGoNext: Boolean,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isLoggingOut: Boolean,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val leftButtonWeight by animateFloatAsState(if (isFirstScreen || isLastScreen) 0.2f else 0.5f)
        val rightButtonWeight by derivedStateOf { 1f - leftButtonWeight }

        when {
            isFirstScreen ->
                LogoutButton(
                    enabled = !isLoggingOut,
                    onClick = onBackClick,
                    modifier = Modifier.weight(leftButtonWeight),
                )

            else ->
                OnboardingBackButton(
                    isLastScreen = isLastScreen,
                    onClick = onBackClick,
                    modifier = Modifier.weight(leftButtonWeight),
                )
        }
        when {
            isLastScreen ->
                OnboardingGeneratePlanButton(
                    onClick = onNextClick,
                    enabled = canGoNext,
                    modifier = Modifier.weight(rightButtonWeight),
                )

            else ->
                OnboardingNextButton(
                    onClick = onNextClick,
                    enabled = canGoNext,
                    modifier = Modifier.weight(rightButtonWeight),
                )
        }
    }
}
