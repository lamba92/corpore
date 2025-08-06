package io.github.lamba92.corpore.app.core.ui.onboarding.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_subtitle
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_title
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_bench_press_1rm_kg
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_bench_press_1rm_pounds
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_calisthenics_can_plank_30_sec
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_calisthenics_has_trained_before
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_calisthenics_max_pushups
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_calisthenics_performance
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_calisthenics_wall_sit_hold
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_deadlift_1rm_kg
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_deadlift_1rm_pounds
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_gym_performance
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_running_15_min_meters
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_running_15_min_yards
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_running_performance
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_squat_1rm_kg
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_squat_1rm_pounds
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_swimming_15_min_meters
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_swimming_15_min_yards
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_swimming_performance
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_swimming_stroke
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_swimming_stroke_backstroke
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_swimming_stroke_breaststroke
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_swimming_stroke_butterfly
import io.github.lamba92.app_core.generated.resources.onboarding_fitness_level_profile_user_input_swimming_stroke_freestyle
import io.github.lamba92.corpore.app.core.ui.components.HorizontalSpacer
import io.github.lamba92.corpore.app.core.ui.components.IntTextField
import io.github.lamba92.corpore.app.core.ui.components.LengthTextField
import io.github.lamba92.corpore.app.core.ui.components.ResourceImage
import io.github.lamba92.corpore.app.core.ui.components.UnitSystemRow
import io.github.lamba92.corpore.app.core.ui.components.WeightTextField
import io.github.lamba92.corpore.app.core.ui.components.defaultTextFieldLabel
import io.github.lamba92.corpore.app.core.ui.components.spacedByThemeInnerPadding
import io.github.lamba92.corpore.app.core.ui.onboarding.OnboardingTitle
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.ui.theme.appMetrics
import io.github.lamba92.corpore.app.core.viewmodel.MeasurementUnitSystem
import io.github.lamba92.corpore.app.core.viewmodel.TrainingPreferences
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingEvent
import io.github.lamba92.corpore.app.core.viewmodel.SportActivity
import io.github.lamba92.corpore.common.core.Length
import io.github.lamba92.corpore.common.core.LengthUnit
import io.github.lamba92.corpore.common.core.Weight
import io.github.lamba92.corpore.common.core.WeightUnit
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun FitnessLevelProfile(
    selectedActivities: Set<SportActivity>,
    currentFitnessLevel: TrainingPreferences.FitnessLevelProfile,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedByThemeInnerPadding(),
    ) {
        OnboardingTitle(
            title = stringResource(Res.string.onboarding_fitness_level_profile_title),
            subtitle = stringResource(Res.string.onboarding_fitness_level_profile_subtitle),
        )
        UnitSystemRow(
            selectedMeasurementUnit = measurementUnitSystem,
            onValueChange = { onUpdate(it.toUpdate()) },
            modifier = Modifier.fillMaxWidth(),
        )
        if (SportActivity.Gym in selectedActivities) {
            GymLevelCard(
                modifier = Modifier.fillMaxWidth(),
                data = currentFitnessLevel.gym,
                measurementUnitSystem = measurementUnitSystem,
                onUpdate = onUpdate,
            )
        }
        if (SportActivity.Running in selectedActivities) {
            RunningLevelCard(
                modifier = Modifier.fillMaxWidth(),
                data = currentFitnessLevel.running,
                measurementUnitSystem = measurementUnitSystem,
                onUpdate = onUpdate,
            )
        }
        if (SportActivity.Swimming in selectedActivities) {
            SwimmingLevelCard(
                modifier = Modifier.fillMaxWidth(),
                data = currentFitnessLevel.swimming,
                measurementUnitSystem = measurementUnitSystem,
                onUpdate = onUpdate,
            )
        }
        if (SportActivity.FreeBody in selectedActivities) {
            FitnessLevelInputCard(
                data = currentFitnessLevel.calisthenics,
                onUpdate = onUpdate,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun FitnessLevelInputCard(
    data: TrainingPreferences.CalisthenicsFitness,
    onUpdate: (OnboardingEvent.FitnessLevelProfile.FreeBody) -> Unit,
    modifier: Modifier = Modifier,
) {
    FitnessLevelCard(
        modifier = modifier,
        title = stringResource(Res.string.onboarding_fitness_level_profile_user_input_calisthenics_performance),
        icon = {
            ResourceImage(
                path = "files/icons/cardio_load_24dp.svg",
                contentDescription = "Calisthenics",
                modifier = Modifier.size(24.dp),
            )
        },
    ) {
        OutlineCheckboxTextButton(
            isSelected = data.hasTrainedBefore,
            onClick = { onUpdate(OnboardingEvent.FitnessLevelProfile.FreeBody.HasTrainedBeforeToggle) },
            text = stringResource(Res.string.onboarding_fitness_level_profile_user_input_calisthenics_has_trained_before),
        )
        IntTextField(
            modifier = Modifier.fillMaxWidth(),
            enabled = data.hasTrainedBefore,
            value = data.maxPushups,
            onValueChange = { onUpdate(OnboardingEvent.FitnessLevelProfile.FreeBody.MaxPushupsChange(it)) },
            label = defaultTextFieldLabel(stringResource(Res.string.onboarding_fitness_level_profile_user_input_calisthenics_max_pushups)),
        )
        IntTextField(
            modifier = Modifier.fillMaxWidth(),
            enabled = data.hasTrainedBefore,
            value = data.wallSitHold.inWholeSeconds.toInt(),
            onValueChange = { onUpdate(OnboardingEvent.FitnessLevelProfile.FreeBody.WallSitHoldChange(it.seconds)) },
            label =
                defaultTextFieldLabel(
                    stringResource(Res.string.onboarding_fitness_level_profile_user_input_calisthenics_wall_sit_hold),
                ),
        )
        OutlineCheckboxTextButton(
            enabled = data.hasTrainedBefore,
            isSelected = data.canPlank30Sec,
            onClick = { onUpdate(OnboardingEvent.FitnessLevelProfile.FreeBody.CanPlank30SecToggle) },
            text = stringResource(Res.string.onboarding_fitness_level_profile_user_input_calisthenics_can_plank_30_sec),
        )
    }
}

@Composable
fun FitnessLevelCard(
    modifier: Modifier = Modifier,
    title: String,
    horizontalOrVertical: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(CorporeTheme.appMetrics.outerPadding / 2),
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
fun GymLevelCard(
    data: TrainingPreferences.GymFitness,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingEvent.FitnessLevelProfile.Gym) -> Unit,
    modifier: Modifier = Modifier,
) {
    FitnessLevelCard(
        modifier = modifier,
        title = stringResource(Res.string.onboarding_fitness_level_profile_user_input_gym_performance),
        icon = {
            ResourceImage(
                path = "files/icons/fitness_center_24dp.svg",
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
fun RunningLevelCard(
    data: TrainingPreferences.RunningFitness,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingEvent.FitnessLevelProfile.Running) -> Unit,
    modifier: Modifier = Modifier,
) {
    FitnessLevelCard(
        modifier = modifier,
        title = stringResource(Res.string.onboarding_fitness_level_profile_user_input_running_performance),
        icon = {
            ResourceImage(
                path = "files/icons/directions_run_24dp.svg",
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
                            MeasurementUnitSystem.Metric -> Res.string.onboarding_fitness_level_profile_user_input_running_15_min_meters
                            MeasurementUnitSystem.Imperial -> Res.string.onboarding_fitness_level_profile_user_input_running_15_min_yards
                        },
                    ),
                ),
        )
    }
}

@Composable
fun SwimmingLevelCard(
    data: TrainingPreferences.SwimmingFitness,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingEvent.FitnessLevelProfile.Swimming) -> Unit,
    modifier: Modifier = Modifier,
) {
    FitnessLevelCard(
        modifier = modifier,
        title = stringResource(Res.string.onboarding_fitness_level_profile_user_input_swimming_performance),
        icon = {
            ResourceImage(
                path = "files/icons/pool_24dp.svg",
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
            text = stringResource(Res.string.onboarding_fitness_level_profile_user_input_swimming_stroke),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        TrainingPreferences.SwimmingFitness.Stroke
            .entries
            .forEach { stroke ->
                SwimmingStrokeButton(
                    enabled = stroke != TrainingPreferences.SwimmingFitness.Stroke.Freestyle,
                    modifier = Modifier.fillMaxWidth(),
                    stroke = stroke,
                    isSelected = stroke in data.knownStrokes,
                    onClick = { onUpdate(stroke.toKnownSwimmingStrokesUpdate(data.knownStrokes)) },
                )
            }
    }
}

private fun TrainingPreferences.SwimmingFitness.Stroke.toKnownSwimmingStrokesUpdate(knownStrokes: Set<TrainingPreferences.SwimmingFitness.Stroke>) =
    when (this) {
        in knownStrokes ->
            OnboardingEvent.FitnessLevelProfile.Swimming.KnownStrokesRemoved(this)

        else ->
            OnboardingEvent.FitnessLevelProfile.Swimming.KnownStrokesAdded(this)
    }

@Composable
fun SwimmingStrokeButton(
    stroke: TrainingPreferences.SwimmingFitness.Stroke,
    isSelected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    OutlineCheckboxTextButton(
        enabled = enabled,
        isSelected = isSelected,
        modifier = modifier,
        onClick = onClick,
        text =
            stringResource(
                when (stroke) {
                    TrainingPreferences.SwimmingFitness.Stroke.Freestyle ->
                        Res.string.onboarding_fitness_level_profile_user_input_swimming_stroke_freestyle

                    TrainingPreferences.SwimmingFitness.Stroke.Backstroke ->
                        Res.string.onboarding_fitness_level_profile_user_input_swimming_stroke_backstroke

                    TrainingPreferences.SwimmingFitness.Stroke.Breaststroke ->
                        Res.string.onboarding_fitness_level_profile_user_input_swimming_stroke_breaststroke

                    TrainingPreferences.SwimmingFitness.Stroke.Butterfly ->
                        Res.string.onboarding_fitness_level_profile_user_input_swimming_stroke_butterfly
                },
            ),
    )
}

@Composable
fun OutlineCheckboxTextButton(
    enabled: Boolean = true,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    enabledTextColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    disabledTextColor: Color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
) {
    OutlineCheckboxButton(
        enabled = enabled,
        modifier = modifier,
        onClick = onClick,
        isSelected = isSelected,
        content = {
            Text(
                text = text,
                style = textStyle,
                color =
                    when {
                        enabled -> enabledTextColor
                        else -> disabledTextColor
                    },
            )
        },
    )
}

@Composable
fun OutlineCheckboxButton(
    enabled: Boolean = true,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    OutlinedButton(
        enabled = enabled,
        modifier = modifier,
        onClick = onClick,
        shape = CorporeTheme.shapes.medium,
        content = {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                content()
                Checkbox(
                    enabled = enabled,
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
    data: TrainingPreferences.SwimmingFitness,
    measurementUnitSystem: MeasurementUnitSystem,
    onUpdate: (OnboardingEvent.FitnessLevelProfile.Swimming) -> Unit,
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
                        MeasurementUnitSystem.Metric -> Res.string.onboarding_fitness_level_profile_user_input_swimming_15_min_meters
                        MeasurementUnitSystem.Imperial -> Res.string.onboarding_fitness_level_profile_user_input_swimming_15_min_yards
                    },
                ),
            ),
    )
}

private fun Length.toFreestyleDistance15MinUpdate() =
    OnboardingEvent.FitnessLevelProfile.Swimming.FreestyleDistance15MinChange(this)

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
                        MeasurementUnitSystem.Metric -> Res.string.onboarding_fitness_level_profile_user_input_deadlift_1rm_kg
                        MeasurementUnitSystem.Imperial -> Res.string.onboarding_fitness_level_profile_user_input_deadlift_1rm_pounds
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
                        MeasurementUnitSystem.Metric -> Res.string.onboarding_fitness_level_profile_user_input_squat_1rm_kg
                        MeasurementUnitSystem.Imperial -> Res.string.onboarding_fitness_level_profile_user_input_squat_1rm_pounds
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
                        MeasurementUnitSystem.Metric -> Res.string.onboarding_fitness_level_profile_user_input_bench_press_1rm_kg
                        MeasurementUnitSystem.Imperial -> Res.string.onboarding_fitness_level_profile_user_input_bench_press_1rm_pounds
                    },
                ),
            ),
    )
}

private fun Weight.toGymBenchPress1RMUpdate() = OnboardingEvent.FitnessLevelProfile.Gym.BenchPress1RMChange(this)

private fun Weight.toGymSquat1RMUpdate() = OnboardingEvent.FitnessLevelProfile.Gym.Squat1RMChange(this)

private fun Weight.toGymDeadlift1RMUpdate() = OnboardingEvent.FitnessLevelProfile.Gym.Deadlift1RMChange(this)

private fun Length.toRunningDistance15MinUpdate() = OnboardingEvent.FitnessLevelProfile.Running.DistanceIn30MinsChange(this)

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
        HorizontalSpacer()
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}
