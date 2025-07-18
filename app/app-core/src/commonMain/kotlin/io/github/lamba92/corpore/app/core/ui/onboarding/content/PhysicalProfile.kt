package io.github.lamba92.corpore.app.core.ui.onboarding.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.baseline_calendar_today_24
import io.github.lamba92.app_core.generated.resources.onboarding_physical_height_cm
import io.github.lamba92.app_core.generated.resources.onboarding_physical_height_feet
import io.github.lamba92.app_core.generated.resources.onboarding_physical_profile_subtitle
import io.github.lamba92.app_core.generated.resources.onboarding_physical_profile_title
import io.github.lamba92.app_core.generated.resources.onboarding_physical_weight_kg
import io.github.lamba92.app_core.generated.resources.onboarding_physical_weight_pounds
import io.github.lamba92.app_core.generated.resources.onboarding_physical_year_of_birth
import io.github.lamba92.corpore.app.core.ui.components.IntTextField
import io.github.lamba92.corpore.app.core.ui.components.LengthTextField
import io.github.lamba92.corpore.app.core.ui.components.UnitSystemRow
import io.github.lamba92.corpore.app.core.ui.components.VerticalSpacer
import io.github.lamba92.corpore.app.core.ui.components.WeightTextField
import io.github.lamba92.corpore.app.core.ui.components.defaultTextFieldLabel
import io.github.lamba92.corpore.app.core.ui.onboarding.OnboardingTitle
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.ui.theme.appMetrics
import io.github.lamba92.corpore.app.core.viewmodel.MeasurementUnitSystem
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingData
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingDataUpdateEvent
import io.github.lamba92.corpore.common.core.Length
import io.github.lamba92.corpore.common.core.LengthUnit
import io.github.lamba92.corpore.common.core.Weight
import io.github.lamba92.corpore.common.core.WeightUnit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PhysicalProfile(
    data: OnboardingData.PhysicalProfile,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingDataUpdateEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        OnboardingTitle(
            title = stringResource(Res.string.onboarding_physical_profile_title),
            subtitle = stringResource(Res.string.onboarding_physical_profile_subtitle),
        )
        VerticalSpacer()
        UnitSystemRow(
            selectedMeasurementUnit = measurementUnitSystem,
            onValueChange = { onUpdate(it.toUpdate()) },
            modifier = Modifier.fillMaxWidth(),
        )
        VerticalSpacer(height = CorporeTheme.appMetrics.innerPadding)
        IntTextField(
            value = data.yearOfBirth,
            onValueChange = { onUpdate(it.toYearOfBirthSelectedUpdate()) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.baseline_calendar_today_24),
                    contentDescription = "calendar icon",
                )
            },
            label = {
                Text(
                    text = stringResource(Res.string.onboarding_physical_year_of_birth),
                    style = MaterialTheme.typography.labelMedium,
                )
            },
        )
        VerticalSpacer(height = CorporeTheme.appMetrics.outerPadding / 2)
        WeightTextField(
            data = data,
            measurementUnitSystem = measurementUnitSystem,
            onUpdate = onUpdate,
        )
        VerticalSpacer(height = CorporeTheme.appMetrics.outerPadding / 2)
        HeightTextField(
            data = data,
            measurementUnitSystem = measurementUnitSystem,
            onUpdate = onUpdate,
        )
    }
}

@Composable
private fun HeightTextField(
    data: OnboardingData.PhysicalProfile,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingDataUpdateEvent) -> Unit,
) {
    LengthTextField(
        length = data.height,
        lengthUnit =
            when (measurementUnitSystem) {
                MeasurementUnitSystem.Metric -> LengthUnit.Centimeters
                MeasurementUnitSystem.Imperial -> LengthUnit.Feet
            },
        onValueChange = { onUpdate(it.toHeightSelectedUpdate()) },
        label =
            defaultTextFieldLabel(
                stringResource(
                    when (measurementUnitSystem) {
                        MeasurementUnitSystem.Metric -> Res.string.onboarding_physical_height_cm
                        MeasurementUnitSystem.Imperial -> Res.string.onboarding_physical_height_feet
                    },
                ),
            ),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun WeightTextField(
    data: OnboardingData.PhysicalProfile,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingDataUpdateEvent) -> Unit,
) {
    WeightTextField(
        weight = data.weight,
        weightUnit =
            when (measurementUnitSystem) {
                MeasurementUnitSystem.Metric -> WeightUnit.Kilograms
                MeasurementUnitSystem.Imperial -> WeightUnit.Pounds
            },
        onValueChange = { onUpdate(it.toWeightSelectedUpdate()) },
        label =
            defaultTextFieldLabel(
                stringResource(
                    when (measurementUnitSystem) {
                        MeasurementUnitSystem.Metric -> Res.string.onboarding_physical_weight_kg
                        MeasurementUnitSystem.Imperial -> Res.string.onboarding_physical_weight_pounds
                    },
                ),
            ),
        modifier = Modifier.fillMaxWidth(),
    )
}

private fun Length.toHeightSelectedUpdate() = OnboardingDataUpdateEvent.PhysicalProfile.HeightSelected(this)

private fun Weight.toWeightSelectedUpdate() = OnboardingDataUpdateEvent.PhysicalProfile.WeightSelected(this)

private fun Int.toYearOfBirthSelectedUpdate() = OnboardingDataUpdateEvent.PhysicalProfile.YearOfBirthSelected(this)

internal fun MeasurementUnitSystem.toUpdate() = OnboardingDataUpdateEvent.MeasurementSystemSelected(measurementUnitSystem = this)
