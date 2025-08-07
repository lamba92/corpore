@file:OptIn(ExperimentalComposeUiApi::class)

package io.github.lamba92.corpore.app.features.onboarding.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import io.github.lamba92.corpore.app.core.ui.components.GradientDirection
import io.github.lamba92.corpore.app.core.ui.components.gradientOverlay
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.ui.theme.appMetrics
import io.github.lamba92.corpore.app.features.onboarding.OnboardingEffects
import io.github.lamba92.corpore.app.features.onboarding.OnboardingEvent
import io.github.lamba92.corpore.app.features.onboarding.OnboardingState
import io.github.lamba92.corpore.app.features.onboarding.OnboardingStepNames
import io.github.lamba92.corpore.app.features.onboarding.OnboardingViewModel
import io.github.lamba92.corpore.app.features.onboarding.isCurrentStepValid
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Onboarding(
    viewModel: OnboardingViewModel = koinViewModel(),
    onLogout: () -> Unit = {},
    onOnboardingComplete: () -> Unit = {},
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel
            .effects
            .collect {
                when (it) {
                    OnboardingEffects.Logout -> onLogout()
                    OnboardingEffects.OnboardingComplete -> onOnboardingComplete()
                }
            }
    }
    Onboarding(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    )
}

@Composable
fun Onboarding(
    state: OnboardingState,
    onEvent: (OnboardingEvent) -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(CorporeTheme.colorScheme.background)
                .padding(top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()),
    ) {
        var onboardingHeaderHeight by remember { mutableStateOf(0.dp) }
        var onboardingFooterHeight by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        OnboardingHeader(
            pageNumber = state.currentStep.ordinal + 1,
            totalPages = OnboardingStepNames.entries.size,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        onboardingHeaderHeight = with(density) { it.size.height.toDp() }
                    }.gradientOverlay(
                        CorporeTheme.colorScheme.background,
                        GradientDirection.BottomToTop,
                    ).padding(
                        vertical = CorporeTheme.appMetrics.innerPadding,
                        horizontal = CorporeTheme.appMetrics.outerPadding,
                    ).zIndex(10f),
        )
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(top = CorporeTheme.appMetrics.outerPadding),
        ) {
            var previousStep by remember { mutableStateOf(state.currentStep) }
            val direction =
                when {
                    state.currentStep.ordinal > previousStep.ordinal -> 1 // Forward
                    state.currentStep.ordinal < previousStep.ordinal -> -1 // Backward
                    else -> 0
                }
            remember(state.currentStep) { previousStep = state.currentStep }
            val onboardingContentScrollingState =
                rememberOnboardingContentScrollingState()
            AnimatedContent(
                targetState = state.currentStep,
                transitionSpec = slideAnimation(direction),
                label = "OnboardingContent",
                modifier = Modifier.fillMaxWidth(),
            ) { target ->
                OnboardingContent(
                    target = target,
                    state = state,
                    onUpdate = onEvent,
                    verticalArrangement = verticalArrangement,
                    horizontalAlignment = horizontalAlignment,
                    onboardingHeaderHeight = onboardingHeaderHeight,
                    onboardingFooterHeight = onboardingFooterHeight,
                    onboardingContentScrollingState = onboardingContentScrollingState,
                )
            }
        }
        OnboardingFooter(
            modifier =
                Modifier
                    .onGloballyPositioned {
                        onboardingFooterHeight = with(density) { it.size.height.toDp() }
                    }.gradientOverlay(
                        CorporeTheme.colorScheme.background,
                        GradientDirection.TopToBottom,
                    ).padding(horizontal = CorporeTheme.appMetrics.outerPadding)
                    .padding(bottom = CorporeTheme.appMetrics.innerPadding)
                    .align(Alignment.BottomCenter)
                    .zIndex(10f),
            isFirstScreen = state.currentStep.ordinal == 0,
            isLastScreen = state.currentStep.ordinal == OnboardingStepNames.entries.lastIndex,
            isLoggingOut = state.isLoggingOut,
            canGoNext = remember(state) { state.isCurrentStepValid() },
            onEvent = onEvent,
        )
    }
}

private fun slideAnimation(direction: Int): AnimatedContentTransitionScope<OnboardingStepNames>.() -> ContentTransform =
    { slideIn(direction) togetherWith slideOut(direction) }

private fun slideOut(direction: Int) = slideOutHorizontally { fullWidth -> -direction * fullWidth }

private fun slideIn(direction: Int) = slideInHorizontally { fullWidth -> direction * fullWidth }
