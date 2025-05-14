package io.github.lamba92.corpore.app.core.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.baseline_calendar_today_24
import io.github.lamba92.app_core.generated.resources.onboarding_physical_height_cm
import io.github.lamba92.app_core.generated.resources.onboarding_physical_height_feet
import io.github.lamba92.app_core.generated.resources.onboarding_physical_profile_subtitle
import io.github.lamba92.app_core.generated.resources.onboarding_physical_profile_title
import io.github.lamba92.app_core.generated.resources.onboarding_physical_weight_kg
import io.github.lamba92.app_core.generated.resources.onboarding_physical_weight_pounds
import io.github.lamba92.app_core.generated.resources.onboarding_physical_year_of_birth
import io.github.lamba92.corpore.app.core.ui.components.LengthTextField
import io.github.lamba92.corpore.app.core.ui.components.UnitSystemRow
import io.github.lamba92.corpore.app.core.ui.components.WeightTextField
import io.github.lamba92.corpore.app.core.ui.components.defaultTextFieldLabel
import io.github.lamba92.corpore.app.core.utils.Length
import io.github.lamba92.corpore.app.core.utils.LengthUnit
import io.github.lamba92.corpore.app.core.utils.Weight
import io.github.lamba92.corpore.app.core.utils.WeightUnit
import io.github.lamba92.corpore.app.core.viewmodel.MeasurementUnitSystem
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingData
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingDataUpdateEvent
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
        Spacer(modifier = Modifier.height(16.dp))
        UnitSystemRow(
            selectedMeasurementUnit = measurementUnitSystem,
            onValueChange = { onUpdate(it.toUpdate()) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        YearOfBirthTextField(
            yearOfBirth = data.yearOfBirth,
            onUpdate = onUpdate,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        WeightTextField(
            data = data,
            measurementUnitSystem = measurementUnitSystem,
            onUpdate = onUpdate,
        )
        Spacer(modifier = Modifier.height(8.dp))
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

@Composable
fun YearOfBirthTextField(
    yearOfBirth: Int?,
    onUpdate: (OnboardingDataUpdateEvent.PhysicalProfile.YearOfBirthSelected) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier,
        value = yearOfBirth?.toString() ?: "",
        onValueChange = {
            it
                .filter { it.isDigit() }
                .toIntOrNull()
                ?.toYearOfBirthSelectedUpdate()
                ?.let(onUpdate)
        },
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
}

private fun Length.toHeightSelectedUpdate() = OnboardingDataUpdateEvent.PhysicalProfile.HeightSelected(this)

private fun Weight.toWeightSelectedUpdate() = OnboardingDataUpdateEvent.PhysicalProfile.WeightSelected(this)

private fun Int.toYearOfBirthSelectedUpdate() = OnboardingDataUpdateEvent.PhysicalProfile.YearOfBirthSelected(this)

internal fun MeasurementUnitSystem.toUpdate() = OnboardingDataUpdateEvent.MeasurementSystemSelected(measurementUnitSystem = this)
