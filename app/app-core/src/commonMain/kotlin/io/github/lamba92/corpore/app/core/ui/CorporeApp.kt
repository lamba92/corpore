package io.github.lamba92.corpore.app.core.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.lamba92.corpore.app.core.di.DiModules
import io.github.lamba92.corpore.app.core.ui.components.WithCoilDebugLogger
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import io.github.lamba92.corpore.app.core.utils.BackHandler
import io.github.lamba92.corpore.app.features.login.components.Login
import io.github.lamba92.corpore.app.features.onboarding.OnboardingViewModel
import io.github.lamba92.corpore.app.features.onboarding.components.Onboarding
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CorporeApp(
    navHostController: NavHostController = rememberNavController(),
    onboardingBackHandler: @Composable (BackHandler) -> Unit = {},
) {
    CorporeTheme {
        KoinApplication(
            application = { modules(DiModules.all) },
            content = {
                WithCoilDebugLogger {
                    NavHost(
                        navController = navHostController,
                        startDestination = "login",
                    ) {
                        composable("login") {
                            Login(
                                onLoginSuccess = {
                                    navHostController.navigate("onboarding") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                            )
                        }
                        composable("onboarding") {
                            val onboardingViewModel: OnboardingViewModel = koinViewModel()
                            onboardingBackHandler { onboardingViewModel.onBackClick() }
                            Onboarding(
                                viewModel = onboardingViewModel,
                                onLogout = {
                                    navHostController.navigate("login") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                },
                                onOnboardingComplete = { /* TODO */ },
                            )
                        }
                    }
                }
            },
        )
    }
}
