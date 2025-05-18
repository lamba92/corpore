package io.github.lamba92.corpore.app.core.ui.onboarding

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
import io.github.lamba92.corpore.app.core.viewmodel.MeasurementUnitSystem
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingData
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingDataUpdateEvent
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingStep
import io.github.lamba92.corpore.app.core.viewmodel.SportActivity

private val defaultOnboardingContentStateMap
    get() = OnboardingStep.entries.associateWith { ScrollState(0) }

class OnboardingContentState(
    private val scrollStateMap: Map<OnboardingStep, ScrollState> = defaultOnboardingContentStateMap,
) {
    fun getScrollState(step: OnboardingStep): ScrollState = scrollStateMap.getValue(step)
}

@Composable
fun rememberOnboardingContentState(initial: Map<OnboardingStep, Int> = emptyMap()): OnboardingContentState =
    remember { OnboardingContentState(defaultOnboardingContentStateMap + initial.mapValues { ScrollState(it.value) }) }

@Composable
fun OnboardingContent(
    target: OnboardingStep,
    data: OnboardingData,
    onUpdate: (OnboardingDataUpdateEvent) -> Unit,
    verticalArrangement: Arrangement.Vertical,
    horizontalAlignment: Alignment.Horizontal,
    onboardingHeaderHeight: Dp,
    onboardingFooterHeight: Dp,
    state: OnboardingContentState = remember { OnboardingContentState() },
) {
    val scrollState = state.getScrollState(target)
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = CorporeTheme.appMetrics.outerPadding),
    ) {
        when (target) {
            OnboardingStep.TrainingLevelSelection ->
                OnboardingContentScaffold(
                    onboardingHeaderHeight = onboardingHeaderHeight,
                    onboardingFooterHeight = onboardingFooterHeight,
                    scrollState = scrollState,
                ) {
                    TrainingLevel(
                        selectedTrainingLevel = data.selectedTrainingLevel,
                        onUpdate = onUpdate,
                        verticalArrangement = verticalArrangement,
                        horizontalAlignment = horizontalAlignment,
                    )
                }

            OnboardingStep.PhysicalProfile ->
                OnboardingContentScaffold(
                    onboardingHeaderHeight = onboardingHeaderHeight,
                    onboardingFooterHeight = onboardingFooterHeight,
                    scrollState = scrollState,
                ) {
                    PhysicalProfile(
                        data = data.physicalProfile,
                        measurementUnitSystem = data.measurementUnitSystem,
                        onUpdate = onUpdate,
                    )
                }

            OnboardingStep.ActivitiesSelection ->
                OnboardingContentScaffold(
                    onboardingHeaderHeight = onboardingHeaderHeight,
                    onboardingFooterHeight = onboardingFooterHeight,
                    scrollState = scrollState,
                ) {
                    ActivitiesSelection(
                        selectedActivities = data.selectedActivities,
                        onUpdate = onUpdate,
                    )
                }

            OnboardingStep.FitnessLevelProfile ->
                OnboardingContentScaffold(
                    onboardingHeaderHeight = onboardingHeaderHeight,
                    onboardingFooterHeight = onboardingFooterHeight,
                    scrollState = scrollState,
                ) {
                    FitnessLevelProfile(
                        selectedActivities = data.selectedActivities.toSet(),
                        currentFitnessLevel = data.fitnessLevelProfile,
                        measurementUnitSystem = data.measurementUnitSystem,
                        onUpdate = onUpdate,
                    )
                }

            OnboardingStep.ActivitiesRotationFrequency ->
                OnboardingContentScaffold(
                    onboardingHeaderHeight = onboardingHeaderHeight,
                    onboardingFooterHeight = onboardingFooterHeight,
                    scrollState = scrollState,
                ) {
                    ActivitiesRotationFrequency(
                        selectedActivities = data.selectedActivities,
                        activitiesRotationFrequency = data.activitiesRotationFrequency,
                        measurementUnitSystem = data.measurementUnitSystem,
                        onUpdate = onUpdate,
                    )
                }
        }
    }
}

@Composable
fun ActivitiesRotationFrequency(
    selectedActivities: Set<SportActivity>,
    activitiesRotationFrequency: OnboardingData.RotationFrequency,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingDataUpdateEvent) -> Unit,
) {
}
