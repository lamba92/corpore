package io.github.lamba92.corpore.app.core.ui.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.baseline_calendar_today_24
import io.github.lamba92.app_core.generated.resources.onboarding_physical_height_cm
import io.github.lamba92.app_core.generated.resources.onboarding_physical_height_feet
import io.github.lamba92.app_core.generated.resources.onboarding_physical_profile_subtitle
import io.github.lamba92.app_core.generated.resources.onboarding_physical_profile_title
import io.github.lamba92.app_core.generated.resources.onboarding_physical_unit_system
import io.github.lamba92.app_core.generated.resources.onboarding_physical_unit_system_imperial
import io.github.lamba92.app_core.generated.resources.onboarding_physical_unit_system_metric
import io.github.lamba92.app_core.generated.resources.onboarding_physical_weight_kg
import io.github.lamba92.app_core.generated.resources.onboarding_physical_weight_pounds
import io.github.lamba92.app_core.generated.resources.onboarding_physical_year_of_birth
import io.github.lamba92.corpore.app.core.ui.components.ToggleButton
import io.github.lamba92.corpore.app.core.utils.Length
import io.github.lamba92.corpore.app.core.utils.LengthUnit
import io.github.lamba92.corpore.app.core.utils.Weight
import io.github.lamba92.corpore.app.core.utils.WeightUnit
import io.github.lamba92.corpore.app.core.utils.toStringWithPrecision
import io.github.lamba92.corpore.app.core.viewmodel.MeasurementSystem
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingData
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingDataUpdateEvent
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PhysicalProfile(
    data: OnboardingData.PhysicalProfile,
    onUpdate: (OnboardingDataUpdateEvent.PhysicalProfile) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        OnboardingTitle(
            title = stringResource(Res.string.onboarding_physical_profile_title),
            subtitle = stringResource(Res.string.onboarding_physical_profile_subtitle),
        )
        Spacer(modifier = Modifier.height(8.dp))
        UnitSystemRow(
            selectedMeasurementUnit = data.measurementSystem,
            onUpdate = onUpdate,
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
            weight = data.weight,
            measurementSystem = data.measurementSystem,
            onUpdate = onUpdate,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        HeightTextField(
            height = data.height,
            measurementSystem = data.measurementSystem,
            onUpdate = onUpdate,
            modifier = Modifier.fillMaxWidth(),
        )
    }
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
        }
    )
}

@Composable
fun WeightTextField(
    weight: Weight?,
    measurementSystem: MeasurementSystem,
    onUpdate: (OnboardingDataUpdateEvent.PhysicalProfile.WeightSelected) -> Unit,
    modifier: Modifier = Modifier,
    ) {
    OutlinedTextField(
        modifier = modifier,
        value = weight?.to(measurementSystem.weightUnit)?.toStringWithPrecision(2) ?: "",
        onValueChange = {
            it
                .toDoubleOrNull()
                ?.toWeightSelectedUpdate(measurementSystem.weightUnit)
                ?.let(onUpdate)
        },
        label = {
            Text(
                text = stringResource(
                    when (measurementSystem) {
                        MeasurementSystem.Metric ->
                            Res.string.onboarding_physical_weight_kg

                        MeasurementSystem.Imperial ->
                            Res.string.onboarding_physical_weight_pounds
                    }
                ),
                style = MaterialTheme.typography.labelMedium,
            )
        },
        trailingIcon = {
            AsyncImage(
                model = Res.getUri("files/icons/monitor_weight_24dp.svg"),
                contentDescription = "weight icon",
                modifier = Modifier.size(24.dp)
            )
        },
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Decimal
        )
    )
}

@Composable
fun HeightTextField(
    height: Length?,
    measurementSystem: MeasurementSystem,
    onUpdate: (OnboardingDataUpdateEvent.PhysicalProfile.HeightSelected) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier,
        value = height?.to(measurementSystem.lengthUnit)?.toStringWithPrecision(2) ?: "",
        onValueChange = {
            it
                .toDoubleOrNull()
                ?.let { toHeightSelectedUpdate(it, measurementSystem.lengthUnit) }
                ?.let(onUpdate)
        },
        label = {
            Text(
                text = stringResource(
                    when (measurementSystem) {
                        MeasurementSystem.Metric ->
                            Res.string.onboarding_physical_height_cm

                        MeasurementSystem.Imperial ->
                            Res.string.onboarding_physical_height_feet
                    }
                ),
                style = MaterialTheme.typography.labelMedium,
            )
        },
        trailingIcon = {
            AsyncImage(
                model = Res.getUri("files/icons/straighten_24dp.svg"),
                contentDescription = "weight icon",
                modifier = Modifier.size(24.dp)
            )
        },
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Decimal
        )
    )
}

private fun toHeightSelectedUpdate(
    it: Double,
    unit: LengthUnit,
) =
    OnboardingDataUpdateEvent.PhysicalProfile.HeightSelected(Length.from(it, unit))

private fun Double.toWeightSelectedUpdate(unit: WeightUnit) =
    OnboardingDataUpdateEvent.PhysicalProfile.WeightSelected(Weight.from(this, unit))

private fun Int.toYearOfBirthSelectedUpdate() =
    OnboardingDataUpdateEvent.PhysicalProfile.YearOfBirthSelected(this)

@Composable
fun UnitSystemRow(
    selectedMeasurementUnit: MeasurementSystem,
    onUpdate: (OnboardingDataUpdateEvent.PhysicalProfile.MeasurementSystemSelected) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.onboarding_physical_unit_system),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        UnitSystemSegmentedButton(
            selectedMeasurementUnit = selectedMeasurementUnit,
            onUpdate = onUpdate,
        )
    }
}

@Composable
fun UnitSystemSegmentedButton(
    selectedMeasurementUnit: MeasurementSystem,
    onUpdate: (OnboardingDataUpdateEvent.PhysicalProfile.MeasurementSystemSelected) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
    ) {
        MeasurementSystem
            .entries
            .forEach { measurementSystem ->
                SegmentedButton(
                    selected = measurementSystem == selectedMeasurementUnit,
                    icon = {},
                    onClick = {
                        onUpdate(
                            OnboardingDataUpdateEvent.PhysicalProfile.MeasurementSystemSelected(
                                measurementSystem = measurementSystem,
                            ),
                        )
                    },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = measurementSystem.ordinal,
                        count = MeasurementSystem.entries.size,
                    ),
                    label = {
                        Text(
                            text =
                                stringResource(
                                    when (measurementSystem) {
                                        MeasurementSystem.Metric ->
                                            Res.string.onboarding_physical_unit_system_metric

                                        MeasurementSystem.Imperial ->
                                            Res.string.onboarding_physical_unit_system_imperial
                                    },
                                ),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                )
            }
    }
}


@Composable
fun UnitSystemToggle(
    selectedMeasurementUnit: MeasurementSystem,
    onUpdate: (OnboardingDataUpdateEvent.PhysicalProfile.MeasurementSystemSelected) -> Unit,
    modifier: Modifier = Modifier,
) {
    ToggleButton(
        selectedIndex = selectedMeasurementUnit.ordinal,
        onItemSelect = {
            onUpdate(
                OnboardingDataUpdateEvent.PhysicalProfile.MeasurementSystemSelected(
                    measurementSystem = MeasurementSystem.entries[it],
                ),
            )
        },
        modifier = modifier,
    ) {
        MeasurementSystem
            .entries
            .forEach { measurementSystem ->
                item { isSelected ->
                    val color by animateColorAsState(
                        targetValue =
                            when {
                                isSelected -> MaterialTheme.colorScheme.onPrimary
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            },
                    )
                    Text(
                        text =
                            stringResource(
                                when (measurementSystem) {
                                    MeasurementSystem.Metric -> Res.string.onboarding_physical_unit_system_metric
                                    MeasurementSystem.Imperial -> Res.string.onboarding_physical_unit_system_imperial
                                },
                            ),
                        style = MaterialTheme.typography.labelSmall,
                        color = color,
                    )
                }
            }
    }
}

