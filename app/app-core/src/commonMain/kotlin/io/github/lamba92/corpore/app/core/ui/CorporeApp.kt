package io.github.lamba92.corpore.app.core.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.lamba92.corpore.app.core.di.DiModules
import io.github.lamba92.corpore.app.core.ui.onboarding.LoginScreen
import io.github.lamba92.corpore.app.core.ui.onboarding.Onboarding
import io.github.lamba92.corpore.app.core.ui.theme.CorporeTheme
import org.koin.compose.KoinApplication

@Composable
fun CorporeApp(navHostController: NavHostController = rememberNavController()) {
    CorporeTheme {
        KoinApplication(
            application = { modules(DiModules.all) },
            content = {
                NavHost(navHostController, "login") {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navHostController.navigate("onboarding") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                        )
                    }
                    composable("onboarding") {
                        Onboarding(
                            onLogout = {
                                navHostController.navigate("login") {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            },
                            onOnboardingComplete = { /* TODO */ },
                        )
                    }
                }
            },
        )
    }
}
