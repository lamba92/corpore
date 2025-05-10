package io.github.lamba92.corpore.app.core.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import org.jetbrains.compose.resources.stringResource

@Composable
fun TrainingLevelSelection(
    selectedTrainingLevel: TrainingLevel?,
    onTrainingLevelClick: (TrainingLevel) -> Unit,
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
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.onboarding_about_yourself_user_level_text),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.FixedSize(100.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false,
        ) {
            items(TrainingLevel.entries) { trainingLevel ->
                TrainingLevelButton(
                    level = trainingLevel,
                    isSelected = selectedTrainingLevel == trainingLevel,
                    onClick = { onTrainingLevelClick(trainingLevel) },
                )
            }
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
        modifier = modifier.aspectRatio(1f),
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
            )
            Spacer(modifier = Modifier.height(8.dp))
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
