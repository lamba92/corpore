package io.github.lamba92.corpore.app.core.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_subtitle
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_title
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_bench_press_1rm_kg
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_bench_press_1rm_pounds
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_deadlift_1rm_kg
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_deadlift_1rm_pounds
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_gym_performance
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_running_15_min_meters
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_running_15_min_yards
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_running_performance
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_squat_1rm_kg
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_squat_1rm_pounds
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_swimming_15_min_meters
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_swimming_15_min_yards
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_swimming_performance
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_swimming_stroke
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_swimming_stroke_backstroke
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_swimming_stroke_breaststroke
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_swimming_stroke_butterfly
import io.github.lamba92.app_core.generated.resources.onboarding_current_fitness_level_user_input_swimming_stroke_freestyle
import io.github.lamba92.corpore.app.core.ui.components.LengthTextField
import io.github.lamba92.corpore.app.core.ui.components.UnitSystemRow
import io.github.lamba92.corpore.app.core.ui.components.WeightTextField
import io.github.lamba92.corpore.app.core.ui.components.defaultTextFieldLabel
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.utils.Length
import io.github.lamba92.corpore.app.core.utils.LengthUnit
import io.github.lamba92.corpore.app.core.utils.Weight
import io.github.lamba92.corpore.app.core.utils.WeightUnit
import io.github.lamba92.corpore.app.core.viewmodel.MeasurementUnitSystem
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingData
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingDataUpdateEvent
import io.github.lamba92.corpore.app.core.viewmodel.SportActivity
import org.jetbrains.compose.resources.stringResource

@Composable
fun CurrentFitnessLevel(
    selectedActivities: Set<SportActivity>,
    currentFitnessLevelUserInputs: OnboardingData.CurrentFitnessLevelUserInputs,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingDataUpdateEvent) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OnboardingTitle(
            title = stringResource(Res.string.onboarding_current_fitness_level_title),
            subtitle = stringResource(Res.string.onboarding_current_fitness_level_subtitle),
        )
        UnitSystemRow(
            selectedMeasurementUnit = measurementUnitSystem,
            onValueChange = { onUpdate(it.toUpdate()) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.size(16.dp))
        if (SportActivity.Gym in selectedActivities) {
            GymLevelInputCard(
                modifier = Modifier.fillMaxWidth(),
                data = currentFitnessLevelUserInputs.gym,
                measurementUnitSystem = measurementUnitSystem,
                onUpdate = onUpdate,
            )
        }
        if (SportActivity.Running in selectedActivities) {
            RunningLevelInputCard(
                modifier = Modifier.fillMaxWidth(),
                data = currentFitnessLevelUserInputs.running,
                measurementUnitSystem = measurementUnitSystem,
                onUpdate = onUpdate,
            )
        }
        if (SportActivity.Swimming in selectedActivities) {
            SwimmingLevelInputCard(
                modifier = Modifier.fillMaxWidth(),
                data = currentFitnessLevelUserInputs.swimming,
                measurementUnitSystem = measurementUnitSystem,
                onUpdate = onUpdate,
            )
        }
    }
}

@Composable
fun CurrentFitnessLevelInputCard(
    modifier: Modifier = Modifier,
    title: String,
    horizontalOrVertical: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(8.dp),
    innerPadding: Dp = 16.dp,
    icon: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.Unspecified),
    ) {
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = horizontalOrVertical,
        ) {
            CardHeader(
                title = title,
                icon = icon,
            )
            content()
        }
    }
}

@Composable
fun GymLevelInputCard(
    data: OnboardingData.GymFitness,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Gym) -> Unit,
    modifier: Modifier = Modifier,
) {
    CurrentFitnessLevelInputCard(
        modifier = modifier,
        title = stringResource(Res.string.onboarding_current_fitness_level_user_input_gym_performance),
        icon = {
            AsyncImage(
                model = Res.getUri("files/icons/fitness_center_24dp.svg"),
                contentDescription = "gym weight",
                modifier = Modifier.size(24.dp),
            )
        },
    ) {
        BenchPressWeightTextField(
            weight = data.benchPress1RM,
            measurementUnitSystem = measurementUnitSystem,
            onValueChange = { onUpdate(it.toGymBenchPress1RMUpdate()) },
        )
        SquatWeightTextField(
            weight = data.squat1RM,
            measurementUnitSystem = measurementUnitSystem,
            onValueChange = { onUpdate(it.toGymSquat1RMUpdate()) },
        )
        DeadliftWeightTextField(
            weight = data.deadlift1RM,
            measurementUnitSystem = measurementUnitSystem,
            onValueChange = { onUpdate(it.toGymDeadlift1RMUpdate()) },
        )
    }
}

@Composable
fun RunningLevelInputCard(
    data: OnboardingData.RunningFitness,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Running) -> Unit,
    modifier: Modifier = Modifier,
) {
    CurrentFitnessLevelInputCard(
        modifier = modifier,
        title = stringResource(Res.string.onboarding_current_fitness_level_user_input_running_performance),
        icon = {
            AsyncImage(
                model = Res.getUri("files/icons/directions_run_24dp.svg"),
                contentDescription = "running",
                modifier = Modifier.size(24.dp),
            )
        },
    ) {
        LengthTextField(
            modifier = Modifier.fillMaxWidth(),
            length = data.distanceIn30Mins,
            lengthUnit =
                when (measurementUnitSystem) {
                    MeasurementUnitSystem.Metric -> LengthUnit.Kilometers
                    MeasurementUnitSystem.Imperial -> LengthUnit.Miles
                },
            onValueChange = { onUpdate(it.toRunningDistance15MinUpdate()) },
            label =
                defaultTextFieldLabel(
                    stringResource(
                        when (measurementUnitSystem) {
                            MeasurementUnitSystem.Metric -> Res.string.onboarding_current_fitness_level_user_input_running_15_min_meters
                            MeasurementUnitSystem.Imperial -> Res.string.onboarding_current_fitness_level_user_input_running_15_min_yards
                        },
                    ),
                ),
        )
    }
}

@Composable
fun SwimmingLevelInputCard(
    data: OnboardingData.SwimmingFitness,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Swimming) -> Unit,
    modifier: Modifier = Modifier,
) {
    CurrentFitnessLevelInputCard(
        modifier = modifier,
        title = stringResource(Res.string.onboarding_current_fitness_level_user_input_swimming_performance),
        icon = {
            AsyncImage(
                model = Res.getUri("files/icons/pool_24dp.svg"),
                contentDescription = "swimming",
                modifier = Modifier.size(24.dp),
            )
        },
    ) {
        FreestyleDistance15MinTextField(
            modifier = Modifier.fillMaxWidth(),
            data = data,
            measurementUnitSystem = measurementUnitSystem,
            onUpdate = onUpdate,
        )
        Text(
            text = stringResource(Res.string.onboarding_current_fitness_level_user_input_swimming_stroke),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        OnboardingData.SwimmingFitness.Stroke
            .entries
            .forEach { stroke ->
                SwimmingStrokeButton(
                    modifier = Modifier.fillMaxWidth(),
                    stroke = stroke,
                    isSelected = stroke in data.knownStrokes,
                    onClick = { onUpdate(stroke.toKnownSwimmingStrokesUpdate(data.knownStrokes)) },
                )
            }
    }
}

private fun OnboardingData.SwimmingFitness.Stroke.toKnownSwimmingStrokesUpdate(knownStrokes: Set<OnboardingData.SwimmingFitness.Stroke>) =
    when (this) {
        in knownStrokes -> OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Swimming.KnownStrokesRemoved(this)
        else -> OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Swimming.KnownStrokesAdded(this)
    }

@Composable
fun SwimmingStrokeButton(
    stroke: OnboardingData.SwimmingFitness.Stroke,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = CorporeTheme.shapes.medium,
        content = {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text =
                        stringResource(
                            when (stroke) {
                                OnboardingData.SwimmingFitness.Stroke.Freestyle ->
                                    Res.string.onboarding_current_fitness_level_user_input_swimming_stroke_freestyle
                                OnboardingData.SwimmingFitness.Stroke.Backstroke ->
                                    Res.string.onboarding_current_fitness_level_user_input_swimming_stroke_backstroke
                                OnboardingData.SwimmingFitness.Stroke.Breaststroke ->
                                    Res.string.onboarding_current_fitness_level_user_input_swimming_stroke_breaststroke
                                OnboardingData.SwimmingFitness.Stroke.Butterfly ->
                                    Res.string.onboarding_current_fitness_level_user_input_swimming_stroke_butterfly
                            },
                        ),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterStart),
                )
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = null,
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }
        },
    )
}

@Composable
fun FreestyleDistance15MinTextField(
    data: OnboardingData.SwimmingFitness,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Swimming) -> Unit,
    modifier: Modifier = Modifier,
) {
    LengthTextField(
        modifier = modifier,
        length = data.freestyleDistance15Min,
        lengthUnit =
            when (measurementUnitSystem) {
                MeasurementUnitSystem.Metric -> LengthUnit.Meters
                MeasurementUnitSystem.Imperial -> LengthUnit.Yards
            },
        onValueChange = { onUpdate(it.toFreestyleDistance15MinUpdate()) },
        label =
            defaultTextFieldLabel(
                stringResource(
                    when (measurementUnitSystem) {
                        MeasurementUnitSystem.Metric -> Res.string.onboarding_current_fitness_level_user_input_swimming_15_min_meters
                        MeasurementUnitSystem.Imperial -> Res.string.onboarding_current_fitness_level_user_input_swimming_15_min_yards
                    },
                ),
            ),
    )
}

private fun Length.toFreestyleDistance15MinUpdate() =
    OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Swimming.FreestyleDistance15MinSelected(this)

@Composable
fun DeadliftWeightTextField(
    weight: Weight,
    measurementUnitSystem: MeasurementUnitSystem,
    onValueChange: (Weight) -> Unit,
) {
    WeightTextField(
        modifier = Modifier.fillMaxWidth(),
        weight = weight,
        weightUnit =
            when (measurementUnitSystem) {
                MeasurementUnitSystem.Metric -> WeightUnit.Kilograms
                MeasurementUnitSystem.Imperial -> WeightUnit.Pounds
            },
        onValueChange = onValueChange,
        label =
            defaultTextFieldLabel(
                stringResource(
                    when (measurementUnitSystem) {
                        MeasurementUnitSystem.Metric -> Res.string.onboarding_current_fitness_level_user_input_deadlift_1rm_kg
                        MeasurementUnitSystem.Imperial -> Res.string.onboarding_current_fitness_level_user_input_deadlift_1rm_pounds
                    },
                ),
            ),
    )
}

@Composable
fun SquatWeightTextField(
    weight: Weight,
    measurementUnitSystem: MeasurementUnitSystem,
    onValueChange: (Weight) -> Unit,
) {
    WeightTextField(
        modifier = Modifier.fillMaxWidth(),
        weight = weight,
        weightUnit =
            when (measurementUnitSystem) {
                MeasurementUnitSystem.Metric -> WeightUnit.Kilograms
                MeasurementUnitSystem.Imperial -> WeightUnit.Pounds
            },
        onValueChange = onValueChange,
        label =
            defaultTextFieldLabel(
                stringResource(
                    when (measurementUnitSystem) {
                        MeasurementUnitSystem.Metric -> Res.string.onboarding_current_fitness_level_user_input_squat_1rm_kg
                        MeasurementUnitSystem.Imperial -> Res.string.onboarding_current_fitness_level_user_input_squat_1rm_pounds
                    },
                ),
            ),
    )
}

@Composable
fun BenchPressWeightTextField(
    weight: Weight,
    measurementUnitSystem: MeasurementUnitSystem,
    onValueChange: (Weight) -> Unit,
) {
    WeightTextField(
        modifier = Modifier.fillMaxWidth(),
        weight = weight,
        weightUnit =
            when (measurementUnitSystem) {
                MeasurementUnitSystem.Metric -> WeightUnit.Kilograms
                MeasurementUnitSystem.Imperial -> WeightUnit.Pounds
            },
        onValueChange = onValueChange,
        label =
            defaultTextFieldLabel(
                stringResource(
                    when (measurementUnitSystem) {
                        MeasurementUnitSystem.Metric -> Res.string.onboarding_current_fitness_level_user_input_bench_press_1rm_kg
                        MeasurementUnitSystem.Imperial -> Res.string.onboarding_current_fitness_level_user_input_bench_press_1rm_pounds
                    },
                ),
            ),
    )
}

private fun Weight.toGymBenchPress1RMUpdate() = OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Gym.BenchPress1RMSelected(this)

private fun Weight.toGymSquat1RMUpdate() = OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Gym.Squat1RMSelected(this)

private fun Weight.toGymDeadlift1RMUpdate() = OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Gym.Deadlift1RMSelected(this)

private fun Length.toRunningDistance15MinUpdate() =
    OnboardingDataUpdateEvent.CurrentFitnessLevelInput.Running.DistanceIn30MinsSelected(this)

@Composable
fun CardHeader(
    modifier: Modifier = Modifier,
    title: String,
    icon: @Composable () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // circle shaped background around icon
        Card(
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            elevation = CardDefaults.elevatedCardElevation(1.dp),
            modifier = Modifier.size(42.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                icon()
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}
