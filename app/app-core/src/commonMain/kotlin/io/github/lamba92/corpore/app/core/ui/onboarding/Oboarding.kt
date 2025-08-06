package io.github.lamba92.corpore.app.core.ui.onboarding

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import io.github.lamba92.corpore.app.core.ui.components.GradientDirection
import io.github.lamba92.corpore.app.core.ui.components.gradientOverlay
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.ui.theme.appMetrics
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingEvent
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingStep
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingViewModel
import io.github.lamba92.corpore.app.core.viewmodel.TrainingPreferences
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    val onboardingData by viewModel.trainingPreferencesStateFlow.collectAsState()
    val currentStep by viewModel.currentOnboardingStepStateFlow.collectAsState()
    val canGoNext by viewModel.canGoNextStateFlow.collectAsState()
    val isLoggingOut by viewModel.isLoggingOutStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel
            .isLoggedInStateFlow
            .filter { !it }
            .onEach { onLogout() }
            .launchIn(this)

        viewModel
            .onboardingCompleteSharedFlow
            .collect { onOnboardingComplete() }
    }

    Onboarding(
        data = onboardingData,
        currentStep = currentStep,
        canGoNext = canGoNext,
        isLoggingOut = isLoggingOut,
        onUpdate = viewModel::onEvent,
        onBackClick = viewModel::onBackClick,
        onNextClick = viewModel::onNextClick,
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    )
}

@Composable
fun Onboarding(
    data: TrainingPreferences,
    currentStep: OnboardingStep,
    canGoNext: Boolean,
    onUpdate: (OnboardingEvent) -> Unit = {},
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    isLoggingOut: Boolean,
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
            pageNumber = currentStep.ordinal + 1,
            totalPages = OnboardingStep.entries.size,
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
            var previousStep by remember { mutableStateOf(currentStep) }
            val direction =
                when {
                    currentStep.ordinal > previousStep.ordinal -> 1 // Forward
                    currentStep.ordinal < previousStep.ordinal -> -1 // Backward
                    else -> 0
                }
            remember(currentStep) { previousStep = currentStep }
            val onboardingContentState = rememberOnboardingContentState()
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = slideAnimation(direction),
                label = "OnboardingContent",
                modifier = Modifier.fillMaxWidth(),
            ) { target ->
                OnboardingContent(
                    target = target,
                    data = data,
                    onUpdate = onUpdate,
                    verticalArrangement = verticalArrangement,
                    horizontalAlignment = horizontalAlignment,
                    onboardingHeaderHeight = onboardingHeaderHeight,
                    onboardingFooterHeight = onboardingFooterHeight,
                    state = onboardingContentState,
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
            isFirstScreen = currentStep.ordinal == 0,
            isLastScreen = currentStep.ordinal == OnboardingStep.entries.lastIndex,
            isLoggingOut = isLoggingOut,
            canGoNext = canGoNext,
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )
    }
}

private fun slideAnimation(direction: Int): AnimatedContentTransitionScope<OnboardingStep>.() -> ContentTransform =
    { slideIn(direction) togetherWith slideOut(direction) }

private fun slideOut(direction: Int) = slideOutHorizontally { fullWidth -> -direction * fullWidth }

private fun slideIn(direction: Int) = slideInHorizontally { fullWidth -> direction * fullWidth }
