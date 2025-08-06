package io.github.lamba92.corpore.app.core.ui.onboarding.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.onboarding_activity_rotation_1_week_plan_subtitle
import io.github.lamba92.app_core.generated.resources.onboarding_activity_rotation_1_week_plan_title
import io.github.lamba92.app_core.generated.resources.onboarding_activity_rotation_more_week_plan_subtitle
import io.github.lamba92.app_core.generated.resources.onboarding_activity_rotation_more_week_plan_title
import io.github.lamba92.app_core.generated.resources.onboarding_activity_rotation_subtitle
import io.github.lamba92.app_core.generated.resources.onboarding_activity_rotation_title
import io.github.lamba92.corpore.app.core.ui.components.HorizontalSpacer
import io.github.lamba92.corpore.app.core.ui.components.VerticalSpacer
import io.github.lamba92.corpore.app.core.ui.components.spacedByThemeInnerPadding
import io.github.lamba92.corpore.app.core.ui.onboarding.OnboardingTitle
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.viewmodel.TrainingPreferences
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingEvent
import org.jetbrains.compose.resources.stringResource

@Composable
fun ActivitiesRotationFrequency(
    selectedRotationFrequency: TrainingPreferences.RotationFrequency,
    onUpdate: (OnboardingEvent.ActivitiesRotationFrequencySelected) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedByThemeInnerPadding(),
    ) {
        OnboardingTitle(
            title = stringResource(Res.string.onboarding_activity_rotation_title),
            subtitle = stringResource(Res.string.onboarding_activity_rotation_subtitle),
        )
        VerticalSpacer()
        ActivitiesRotationFrequencyButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            title = stringResource(Res.string.onboarding_activity_rotation_1_week_plan_title),
            bodyText = stringResource(Res.string.onboarding_activity_rotation_1_week_plan_subtitle),
            isSelected = selectedRotationFrequency == TrainingPreferences.RotationFrequency.Weekly,
            onClick = { onUpdate(TrainingPreferences.RotationFrequency.Weekly.toWeeklyRotationUpdate()) },
        )
        ActivitiesRotationFrequencyButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            title = stringResource(Res.string.onboarding_activity_rotation_more_week_plan_title, 2),
            bodyText = stringResource(Res.string.onboarding_activity_rotation_more_week_plan_subtitle, 2),
            isSelected = selectedRotationFrequency == TrainingPreferences.RotationFrequency.BiWeekly,
            onClick = { onUpdate(TrainingPreferences.RotationFrequency.BiWeekly.toWeeklyRotationUpdate()) },
        )
        ActivitiesRotationFrequencyButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            title = stringResource(Res.string.onboarding_activity_rotation_more_week_plan_title, 4),
            bodyText = stringResource(Res.string.onboarding_activity_rotation_more_week_plan_subtitle, 4),
            isSelected = selectedRotationFrequency == TrainingPreferences.RotationFrequency.Monthly,
            onClick = { onUpdate(TrainingPreferences.RotationFrequency.Monthly.toWeeklyRotationUpdate()) },
        )
    }
}

private fun TrainingPreferences.RotationFrequency.toWeeklyRotationUpdate() = OnboardingEvent.ActivitiesRotationFrequencySelected(this)

@Composable
fun ActivitiesRotationFrequencyButton(
    title: String,
    bodyText: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = CorporeTheme.shapes.medium,
        content = {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = null,
                )
                HorizontalSpacer()
                Column {
                    Text(
                        text = title,
                        style = CorporeTheme.typography.titleLarge,
                    )
                    Text(
                        text = bodyText,
                        style = CorporeTheme.typography.bodyMedium,
                        color = CorporeTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
    )
}
