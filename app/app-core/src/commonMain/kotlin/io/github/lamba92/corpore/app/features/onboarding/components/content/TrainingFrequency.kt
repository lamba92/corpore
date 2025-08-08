package io.github.lamba92.corpore.app.features.onboarding.components.content

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
import io.github.lamba92.app_core.generated.resources.onboarding_training_frequency_more
import io.github.lamba92.app_core.generated.resources.onboarding_training_frequency_once
import io.github.lamba92.app_core.generated.resources.onboarding_training_frequency_subtitle
import io.github.lamba92.app_core.generated.resources.onboarding_training_frequency_title
import io.github.lamba92.app_core.generated.resources.onboarding_training_frequency_twice
import io.github.lamba92.corpore.app.core.ui.components.HorizontalSpacer
import io.github.lamba92.corpore.app.core.ui.components.VerticalSpacer
import io.github.lamba92.corpore.app.core.ui.components.spacedByThemeInnerPadding
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.features.onboarding.OnboardingEvent
import io.github.lamba92.corpore.app.features.onboarding.OnboardingState
import io.github.lamba92.corpore.common.core.data.TrainingFrequency
import io.github.lamba92.corpore.app.features.onboarding.components.OnboardingTitle
import org.jetbrains.compose.resources.stringResource

@Composable
fun TrainingFrequency(
    data: OnboardingState.TrainingFrequencyStep,
    onUpdate: (OnboardingEvent.TrainingFrequencySelected) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedByThemeInnerPadding(),
    ) {
        OnboardingTitle(
            title = stringResource(Res.string.onboarding_training_frequency_title),
            subtitle = stringResource(Res.string.onboarding_training_frequency_subtitle),
        )
        VerticalSpacer()
        TrainingFrequency.entries.forEach { frequency ->
            val title =
                when (frequency) {
                    TrainingFrequency.ONCE -> stringResource(Res.string.onboarding_training_frequency_once)
                    TrainingFrequency.TWICE -> stringResource(Res.string.onboarding_training_frequency_twice)
                    else ->
                        stringResource(
                            Res.string.onboarding_training_frequency_more,
                            frequency.ordinal + 1,
                        )
                }
            TrainingFrequencyButton(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                title = title,
                isSelected = data.frequency == frequency,
                onClick = { onUpdate(OnboardingEvent.TrainingFrequencySelected(frequency)) },
            )
        }
    }
}

@Composable
fun TrainingFrequencyButton(
    title: String,
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
                Text(
                    text = title,
                    style = CorporeTheme.typography.titleLarge,
                )
            }
        },
    )
}
