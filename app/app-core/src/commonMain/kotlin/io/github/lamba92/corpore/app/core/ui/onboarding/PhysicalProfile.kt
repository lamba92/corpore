package io.github.lamba92.corpore.app.core.ui.onboarding

import androidx.compose.runtime.Composable
import io.github.lamba92.corpore.app.core.utils.Length
import io.github.lamba92.corpore.app.core.utils.Weight
import io.github.lamba92.corpore.app.core.viewmodel.MeasurementSystem
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingDataUpdateEvent

@Composable
fun PhysicalProfile(
    yearOfBirth: Int?,
    weight: Weight?,
    height: Length?,
    measurementUnit: MeasurementSystem,
    onUpdate: (OnboardingDataUpdateEvent.PhysicalProfile) -> Unit,
) {
    OnboardingTitle(
        title = "Physical Profile",
        subtitle = "Please provide your physical profile information.",
    )
}
