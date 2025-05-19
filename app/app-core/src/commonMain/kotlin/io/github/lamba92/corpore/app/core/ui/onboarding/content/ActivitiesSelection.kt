package io.github.lamba92.corpore.app.core.ui.onboarding.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.lamba92.app_core.generated.resources.Res
import io.github.lamba92.app_core.generated.resources.onboarding_sport_activity_calisthenics
import io.github.lamba92.app_core.generated.resources.onboarding_sport_activity_free_subtitle
import io.github.lamba92.app_core.generated.resources.onboarding_sport_activity_free_title
import io.github.lamba92.app_core.generated.resources.onboarding_sport_activity_gym
import io.github.lamba92.app_core.generated.resources.onboarding_sport_activity_running
import io.github.lamba92.app_core.generated.resources.onboarding_sport_activity_swimming
import io.github.lamba92.corpore.app.core.ui.components.HorizontalSpacer
import io.github.lamba92.corpore.app.core.ui.components.VerticalSpacer
import io.github.lamba92.corpore.app.core.ui.components.spacedByThemeInnerPadding
import io.github.lamba92.corpore.app.core.ui.onboarding.OnboardingTitle
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingDataUpdateEvent
import io.github.lamba92.corpore.app.core.viewmodel.SportActivity
import org.jetbrains.compose.resources.stringResource

@Composable
fun ActivitiesSelection(
    selectedActivities: Set<SportActivity>,
    onUpdate: (OnboardingDataUpdateEvent.ActivitiesSelection) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        OnboardingTitle(
            title = stringResource(Res.string.onboarding_sport_activity_free_title),
            subtitle = stringResource(Res.string.onboarding_sport_activity_free_subtitle),
        )
        VerticalSpacer()
        Column(
            verticalArrangement = Arrangement.spacedByThemeInnerPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SportActivity
                .entries
                .forEach { activity ->
                    SportActivity(
                        activity = activity,
                        isSelected = activity in selectedActivities,
                        onClick = {
                            when (activity) {
                                in selectedActivities -> onUpdate(activity.toActivityRemovedUpdate())
                                else -> onUpdate(activity.toActivityAddedUpdate())
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
        }
    }
}

private fun SportActivity.toActivityRemovedUpdate() = OnboardingDataUpdateEvent.ActivitiesSelection.ActivityRemoved(listOf(this))

private fun SportActivity.toActivityAddedUpdate() = OnboardingDataUpdateEvent.ActivitiesSelection.ActivityAdded(listOf(this))

@Composable
fun SportActivity(
    activity: SportActivity,
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
                AsyncImage(
                    model =
                        Res.getUri(
                            path =
                                "files/icons/" +
                                    when (activity) {
                                        SportActivity.Gym -> "fitness_center_24dp.svg"
                                        SportActivity.Running -> "directions_run_24dp.svg"
                                        SportActivity.Swimming -> "pool_24dp.svg"
                                        SportActivity.FreeBody -> "cardio_load_24dp.svg"
                                    },
                        ),
                    contentDescription = activity.name,
                    modifier = Modifier.size(48.dp),
                )
                HorizontalSpacer()
                Text(
                    text =
                        stringResource(
                            resource =
                                when (activity) {
                                    SportActivity.Gym -> Res.string.onboarding_sport_activity_gym
                                    SportActivity.Running -> Res.string.onboarding_sport_activity_running
                                    SportActivity.Swimming -> Res.string.onboarding_sport_activity_swimming
                                    SportActivity.FreeBody -> Res.string.onboarding_sport_activity_calisthenics
                                },
                        ),
                    style = CorporeTheme.typography.bodyLarge,
                    color = CorporeTheme.colorScheme.onBackground,
                )
            }
            Checkbox(
                checked = isSelected,
                onCheckedChange = null,
            )
        },
    )
}
