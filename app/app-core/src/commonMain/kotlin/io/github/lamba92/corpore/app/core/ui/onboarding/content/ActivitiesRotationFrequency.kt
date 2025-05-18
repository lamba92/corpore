package io.github.lamba92.corpore.app.core.ui.onboarding.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import io.github.lamba92.corpore.app.core.ui.onboarding.OnboardingTitle
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.ui.theme.appMetrics
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
        verticalArrangement = Arrangement.spacedBy(CorporeTheme.appMetrics.innerPadding),
    ) {
        OnboardingTitle(
            title = stringResource(Res.string.onboarding_activity_rotation_title),
            subtitle = stringResource(Res.string.onboarding_activity_rotation_subtitle),
        )
        VerticalSpacer()
        ActivitiesRotationFrequencyCard(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.onboarding_activity_rotation_1_week_plan_title),
            bodyText = stringResource(Res.string.onboarding_activity_rotation_1_week_plan_subtitle),
            isSelected = selectedRotationFrequency == OnboardingData.RotationFrequency.Weekly,
            onClick = { onUpdate(OnboardingData.RotationFrequency.Weekly.toWeeklyRotationUpdate()) },
        )
        ActivitiesRotationFrequencyCard(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.onboarding_activity_rotation_more_week_plan_title, 2),
            bodyText = stringResource(Res.string.onboarding_activity_rotation_more_week_plan_subtitle, 2),
            isSelected = selectedRotationFrequency == OnboardingData.RotationFrequency.BiWeekly,
            onClick = { onUpdate(OnboardingData.RotationFrequency.BiWeekly.toWeeklyRotationUpdate()) },
        )
        ActivitiesRotationFrequencyCard(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.onboarding_activity_rotation_more_week_plan_title, 4),
            bodyText = stringResource(Res.string.onboarding_activity_rotation_more_week_plan_subtitle, 4),
            isSelected = selectedRotationFrequency == OnboardingData.RotationFrequency.Monthly,
            onClick = { onUpdate(OnboardingData.RotationFrequency.Monthly.toWeeklyRotationUpdate()) },
        )
    }
}

private fun OnboardingData.RotationFrequency.toWeeklyRotationUpdate() = OnboardingDataUpdateEvent.ActivitiesRotationFrequencySelected(this)

@Composable
fun ActivitiesRotationFrequencyCard(
    title: String,
    bodyText: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.Unspecified),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(CorporeTheme.appMetrics.innerPadding),
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
    }
}
