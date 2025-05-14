package io.github.lamba92.corpore.app.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.onboarding_physical_unit_system
import io.github.lamba92.app_core.generated.resources.onboarding_physical_unit_system_imperial
import io.github.lamba92.app_core.generated.resources.onboarding_physical_unit_system_metric
import io.github.lamba92.corpore.app.core.viewmodel.MeasurementUnitSystem
import org.jetbrains.compose.resources.stringResource

@Composable
fun UnitSystemRow(
    selectedMeasurementUnit: MeasurementUnitSystem,
    onValueChange: (MeasurementUnitSystem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.onboarding_physical_unit_system),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        UnitSystemSegmentedButton(
            selectedMeasurementUnit = selectedMeasurementUnit,
            onValueChange = onValueChange,
        )
    }
}

@Composable
fun UnitSystemSegmentedButton(
    selectedMeasurementUnit: MeasurementUnitSystem,
    onValueChange: (MeasurementUnitSystem) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
    ) {
        MeasurementUnitSystem
            .entries
            .forEach { measurementSystem ->
                SegmentedButton(
                    selected = measurementSystem == selectedMeasurementUnit,
                    icon = {},
                    onClick = { onValueChange(measurementSystem) },
                    shape =
                        SegmentedButtonDefaults.itemShape(
                            index = measurementSystem.ordinal,
                            count = MeasurementUnitSystem.entries.size,
                        ),
                    label = {
                        Text(
                            text =
                                stringResource(
                                    when (measurementSystem) {
                                        MeasurementUnitSystem.Metric ->
                                            Res.string.onboarding_physical_unit_system_metric

                                        MeasurementUnitSystem.Imperial ->
                                            Res.string.onboarding_physical_unit_system_imperial
                                    },
                                ),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                )
            }
    }
}

@Composable
fun UnitSystemToggle(
    selectedMeasurementUnit: MeasurementUnitSystem,
    onUpdate: (MeasurementUnitSystem) -> Unit,
    modifier: Modifier = Modifier,
) {
    ToggleButton(
        selectedIndex = selectedMeasurementUnit.ordinal,
        onItemSelect = { onUpdate(MeasurementUnitSystem.entries[it]) },
        modifier = modifier,
    ) {
        MeasurementUnitSystem
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
                                    MeasurementUnitSystem.Metric -> Res.string.onboarding_physical_unit_system_metric
                                    MeasurementUnitSystem.Imperial -> Res.string.onboarding_physical_unit_system_imperial
                                },
                            ),
                        style = MaterialTheme.typography.labelSmall,
                        color = color,
                    )
                }
            }
    }
}
