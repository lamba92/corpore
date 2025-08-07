package io.github.lamba92.corpore.app.features.onboarding.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.ui.theme.appMetrics
import io.github.lamba92.corpore.app.features.onboarding.OnboardingEvent
import io.github.lamba92.corpore.app.features.onboarding.OnboardingState
import io.github.lamba92.corpore.app.features.onboarding.OnboardingStepNames
import io.github.lamba92.corpore.app.features.onboarding.components.content.ActivitiesRotationFrequency
import io.github.lamba92.corpore.app.features.onboarding.components.content.ActivitiesSelection
import io.github.lamba92.corpore.app.features.onboarding.components.content.FitnessLevelProfile
import io.github.lamba92.corpore.app.features.onboarding.components.content.PhysicalProfile
import io.github.lamba92.corpore.app.features.onboarding.components.content.TrainingLevel

private val defaultOnboardingContentStateMap
    get() = OnboardingStepNames.entries.associateWith { ScrollState(0) }

class OnboardingContentScrollingState(
    private val scrollStateMap: Map<OnboardingStepNames, ScrollState> = defaultOnboardingContentStateMap,
) {
    fun getScrollState(step: OnboardingStepNames): ScrollState = scrollStateMap.getValue(step)
}

@Composable
fun rememberOnboardingContentScrollingState(initial: Map<OnboardingStepNames, Int> = emptyMap()): OnboardingContentScrollingState =
    remember { OnboardingContentScrollingState(defaultOnboardingContentStateMap + initial.mapValues { ScrollState(it.value) }) }

@Composable
fun OnboardingContent(
    target: OnboardingStepNames,
    state: OnboardingState,
    onUpdate: (OnboardingEvent) -> Unit,
    verticalArrangement: Arrangement.Vertical,
    horizontalAlignment: Alignment.Horizontal,
    onboardingHeaderHeight: Dp,
    onboardingFooterHeight: Dp,
    onboardingContentScrollingState: OnboardingContentScrollingState = remember { OnboardingContentScrollingState() },
) {
    val scrollState = onboardingContentScrollingState.getScrollState(target)
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = CorporeTheme.appMetrics.outerPadding),
    ) {
        when (target) {
            OnboardingStepNames.TrainingLevelSelection ->
                OnboardingContentScaffold(
                    onboardingHeaderHeight = onboardingHeaderHeight,
                    onboardingFooterHeight = onboardingFooterHeight,
                    scrollState = scrollState,
                ) {
                    TrainingLevel(
                        selectedTrainingLevel = state.stepsData.trainingLevelSelection.level,
                        onUpdate = onUpdate,
                        verticalArrangement = verticalArrangement,
                        horizontalAlignment = horizontalAlignment,
                    )
                }

            OnboardingStepNames.PhysicalProfile ->
                OnboardingContentScaffold(
                    onboardingHeaderHeight = onboardingHeaderHeight,
                    onboardingFooterHeight = onboardingFooterHeight,
                    scrollState = scrollState,
                ) {
                    PhysicalProfile(
                        data = state.stepsData.physicalProfile,
                        measurementUnitSystem = state.measurementUnitSystem,
                        onUpdate = onUpdate,
                    )
                }

            OnboardingStepNames.ActivitiesSelection ->
                OnboardingContentScaffold(
                    onboardingHeaderHeight = onboardingHeaderHeight,
                    onboardingFooterHeight = onboardingFooterHeight,
                    scrollState = scrollState,
                ) {
                    ActivitiesSelection(
                        selectedActivities = state.stepsData.activitiesSelection.activities,
                        onUpdate = onUpdate,
                    )
                }

            OnboardingStepNames.FitnessLevelProfile ->
                OnboardingContentScaffold(
                    onboardingHeaderHeight = onboardingHeaderHeight,
                    onboardingFooterHeight = onboardingFooterHeight,
                    scrollState = scrollState,
                ) {
                    FitnessLevelProfile(
                        selectedActivities = state.stepsData.activitiesSelection.activities,
                        currentFitnessLevel = state.stepsData.fitnessLevelProfile,
                        measurementUnitSystem = state.measurementUnitSystem,
                        onUpdate = onUpdate,
                    )
                }

            OnboardingStepNames.ActivitiesRotationFrequency ->
                OnboardingContentScaffold(
                    onboardingHeaderHeight = onboardingHeaderHeight,
                    onboardingFooterHeight = onboardingFooterHeight,
                    scrollState = scrollState,
                ) {
                    ActivitiesRotationFrequency(
                        data = state.stepsData.rotationFrequency,
                        onUpdate = onUpdate,
                    )
                }
        }
    }
}
