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
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingData
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingDataUpdateEvent
import org.jetbrains.compose.resources.stringResource

@Composable
fun ActivitiesRotationFrequency(
    selectedRotationFrequency: OnboardingData.RotationFrequency,
    onUpdate: (OnboardingDataUpdateEvent.ActivitiesRotationFrequencySelected) -> Unit,
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
            isSelected = selectedRotationFrequency == OnboardingData.RotationFrequency.Weekly,
            onClick = { onUpdate(OnboardingData.RotationFrequency.Weekly.toWeeklyRotationUpdate()) },
        )
        ActivitiesRotationFrequencyButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            title = stringResource(Res.string.onboarding_activity_rotation_more_week_plan_title, 2),
            bodyText = stringResource(Res.string.onboarding_activity_rotation_more_week_plan_subtitle, 2),
            isSelected = selectedRotationFrequency == OnboardingData.RotationFrequency.BiWeekly,
            onClick = { onUpdate(OnboardingData.RotationFrequency.BiWeekly.toWeeklyRotationUpdate()) },
        )
        ActivitiesRotationFrequencyButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            title = stringResource(Res.string.onboarding_activity_rotation_more_week_plan_title, 4),
            bodyText = stringResource(Res.string.onboarding_activity_rotation_more_week_plan_subtitle, 4),
            isSelected = selectedRotationFrequency == OnboardingData.RotationFrequency.Monthly,
            onClick = { onUpdate(OnboardingData.RotationFrequency.Monthly.toWeeklyRotationUpdate()) },
        )
    }
}

private fun OnboardingData.RotationFrequency.toWeeklyRotationUpdate() = OnboardingDataUpdateEvent.ActivitiesRotationFrequencySelected(this)

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
