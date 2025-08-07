package io.github.lamba92.corpore.app.features.onboarding.components.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.onboarding_about_yourself_user_level_subtitle
import io.github.lamba92.app_core.generated.resources.onboarding_about_yourself_user_level_text
import io.github.lamba92.app_core.generated.resources.onboarding_about_yourself_user_level_title
import io.github.lamba92.app_core.generated.resources.onboarding_user_level_advanced
import io.github.lamba92.app_core.generated.resources.onboarding_user_level_beginner
import io.github.lamba92.app_core.generated.resources.onboarding_user_level_intermediate
import io.github.lamba92.app_core.generated.resources.onboarding_user_level_pro
import io.github.lamba92.corpore.app.core.ui.components.VerticalSpacer
import io.github.lamba92.corpore.app.core.ui.components.spacedByThemeInnerPadding
import io.github.lamba92.corpore.app.core.ui.components.spacedByThemeOuterPadding
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.ui.theme.appMetrics
import io.github.lamba92.corpore.app.features.onboarding.OnboardingEvent
import io.github.lamba92.corpore.app.features.onboarding.components.OnboardingTitle
import org.jetbrains.compose.resources.stringResource

@Composable
fun TrainingLevel(
    selectedTrainingLevel: TrainingLevel?,
    onUpdate: (OnboardingEvent.TrainingLevelSelected) -> Unit,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        OnboardingTitle(
            title = stringResource(Res.string.onboarding_about_yourself_user_level_title),
            subtitle = stringResource(Res.string.onboarding_about_yourself_user_level_subtitle),
        )
        VerticalSpacer()
        Text(
            text = stringResource(Res.string.onboarding_about_yourself_user_level_text),
            style = MaterialTheme.typography.bodyMedium,
        )
        VerticalSpacer()
        TrainingLevelSelectionButtons(
            selectedTrainingLevel = selectedTrainingLevel,
            onTrainingLevelClick = { onUpdate(OnboardingEvent.TrainingLevelSelected(it)) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
fun TrainingLevelSelectionButtons(
    selectedTrainingLevel: TrainingLevel?,
    onTrainingLevelClick: (TrainingLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedByThemeInnerPadding(),
    ) {
        TrainingLevelSelectionButtonsRow(
            selectedTrainingLevel = selectedTrainingLevel,
            onTrainingLevelClick = onTrainingLevelClick,
            levels = arrayOf(TrainingLevel.Beginner, TrainingLevel.Intermediate),
            modifier = Modifier.fillMaxWidth(),
        )
        TrainingLevelSelectionButtonsRow(
            selectedTrainingLevel = selectedTrainingLevel,
            onTrainingLevelClick = onTrainingLevelClick,
            levels = arrayOf(TrainingLevel.Advanced, TrainingLevel.Pro),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun TrainingLevelSelectionButtonsRow(
    selectedTrainingLevel: TrainingLevel?,
    onTrainingLevelClick: (TrainingLevel) -> Unit,
    modifier: Modifier = Modifier,
    vararg levels: TrainingLevel,
) {
    Row(
        horizontalArrangement = Arrangement.spacedByThemeOuterPadding(),
        modifier = modifier,
    ) {
        levels.forEach { level ->
            TrainingLevelButton(
                level = level,
                isSelected = selectedTrainingLevel == level,
                onClick = { onTrainingLevelClick(level) },
                modifier = Modifier.weight(0.5f),
            )
        }
    }
}

enum class TrainingLevel {
    Beginner,
    Intermediate,
    Advanced,
    Pro,
}

@Composable
fun TrainingLevelButton(
    level: TrainingLevel,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                contentColor =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
            ),
        border =
            BorderStroke(
                width = 1.dp,
                color =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
            ),
        // square shape
        shape = MaterialTheme.shapes.small,
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                model =
                    when (level) {
                        TrainingLevel.Beginner -> Res.getUri("files/icons/rocket_24dp.svg")
                        TrainingLevel.Intermediate -> Res.getUri("files/icons/exercise_24dp.svg")
                        TrainingLevel.Advanced -> Res.getUri("files/icons/license_24dp.svg")
                        TrainingLevel.Pro -> Res.getUri("files/icons/trophy_24dp.svg")
                    },
                contentDescription = null,
                modifier = Modifier.size(48.dp),
            )
            VerticalSpacer(height = CorporeTheme.appMetrics.outerPadding / 2)
            val resourceId =
                when (level) {
                    TrainingLevel.Beginner -> Res.string.onboarding_user_level_beginner
                    TrainingLevel.Intermediate -> Res.string.onboarding_user_level_intermediate
                    TrainingLevel.Advanced -> Res.string.onboarding_user_level_advanced
                    TrainingLevel.Pro -> Res.string.onboarding_user_level_pro
                }
            Text(text = stringResource(resourceId))
        }
    }
}
