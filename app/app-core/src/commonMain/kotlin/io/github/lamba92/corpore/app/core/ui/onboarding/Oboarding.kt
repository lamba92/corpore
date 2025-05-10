package io.github.lamba92.corpore.app.core.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.app_name
import io.github.lamba92.app_core.generated.resources.baseline_arrow_back_24
import io.github.lamba92.app_core.generated.resources.baseline_logout_24
import io.github.lamba92.app_core.generated.resources.onboarding_back
import io.github.lamba92.app_core.generated.resources.onboarding_next
import io.github.lamba92.app_core.generated.resources.outline_arrow_forward_24
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.ui.theme.appMetrics
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingData
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingDataUpdateEvent
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingStep
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    buttonColors: ButtonColors,
    content:
    @Composable (RowScope.() -> Unit),
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
            text = stringResource(Res.string.onboarding_next)
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
        Text(stringResource(Res.string.onboarding_next))
        Image(
            painter = painterResource(Res.drawable.outline_arrow_forward_24),
            contentDescription = "Arrow forward icon",
        )
    }
}

@Composable
fun OnboardingBackButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
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
        Text(
            text = stringResource(Res.string.onboarding_back),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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

@Composable
fun Onboarding(
    viewModel: OnboardingViewModel = koinViewModel(),
    onLogout: () -> Unit = {},
    onOnboardingComplete: () -> Unit = {},
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    val onboardingData by viewModel.onboardingDataStateFlow.collectAsState()
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
        onboardingUpdate = viewModel::update,
        onBackClick = viewModel::onBackClick,
        onNextClick = viewModel::onNextClick,
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    )
}

@Composable
fun Onboarding(
    data: OnboardingData,
    currentStep: OnboardingStep,
    canGoNext: Boolean,
    onboardingUpdate: (OnboardingDataUpdateEvent) -> Unit = {},
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    isLoggingOut: Boolean,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(CorporeTheme.colorScheme.background)
                .padding(vertical = CorporeTheme.appMetrics.outerPadding),
    ) {
        OnboardingHeader(
            pageNumber = currentStep.ordinal + 1,
            totalPages = OnboardingStep.entries.size,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = CorporeTheme.appMetrics.innerPadding,
                        horizontal = CorporeTheme.appMetrics.outerPadding,
                    ),
        )
        var previousStep by remember { mutableStateOf(currentStep) }
        val direction =
            when {
                currentStep.ordinal > previousStep.ordinal -> 1 // Forward
                currentStep.ordinal < previousStep.ordinal -> -1 // Backward
                else -> 0
            }
        LaunchedEffect(currentStep) {
            previousStep = currentStep
        }
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = slideAnimation(direction),
            label = "CarouselAnimation",
            modifier = Modifier.weight(1f),
        ) { target ->
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(horizontal = CorporeTheme.appMetrics.outerPadding),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment,
            ) {
                when (target) {
                    OnboardingStep.TrainingLevelSelection ->
                        TrainingLevelSelection(
                            selectedTrainingLevel = data.selectedTrainingLevel,
                            onTrainingLevelClick = { onboardingUpdate(it.toUpdate()) },
                        )

                    OnboardingStep.UserData -> {} // UserData()
                    OnboardingStep.ActivitiesSelection -> {} // ActivitiesSelection()
                    OnboardingStep.CurrentFitnessLevelUserInput -> {} // CurrentFitnessLevelUserInput()
                    OnboardingStep.ActivitiesRotationFrequency -> {} // ActivitiesRotationFrequency()
                }
            }
        }
        OnboardingFooter(
            modifier = Modifier.padding(horizontal = CorporeTheme.appMetrics.outerPadding),
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

private fun TrainingLevel.toUpdate() = OnboardingDataUpdateEvent.TrainingLevelSelected(this)

@Composable
private fun OnboardingFooter(
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

@Composable
fun OnboardingTitle(
    title: String,
    subtitle: String,
) {
    Text(
        text = title,
        style = CorporeTheme.typography.titleMedium,
        color = CorporeTheme.colorScheme.onBackground,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = subtitle,
        style = CorporeTheme.typography.bodyMedium,
        color = CorporeTheme.colorScheme.onBackground,
    )
}

@Composable
fun OnboardingHeader(
    pageNumber: Int,
    totalPages: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Text(
            text = stringResource(Res.string.app_name),
            style = CorporeTheme.typography.bodyLarge,
            color = CorporeTheme.colorScheme.primary,
        )
        Text(
            text = "$pageNumber/$totalPages",
            style = CorporeTheme.typography.bodySmall,
            color = CorporeTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}
