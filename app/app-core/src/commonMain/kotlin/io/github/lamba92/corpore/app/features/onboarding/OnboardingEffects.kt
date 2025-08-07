package io.github.lamba92.corpore.app.features.onboarding

import kotlinx.serialization.Serializable

@Serializable
sealed interface OnboardingEffects {
    @Serializable
    object OnboardingComplete : OnboardingEffects

    @Serializable
    object Logout : OnboardingEffects
}
