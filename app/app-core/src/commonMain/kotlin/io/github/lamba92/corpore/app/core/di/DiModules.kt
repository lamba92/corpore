package io.github.lamba92.corpore.app.core.di

import io.github.lamba92.corpore.app.core.repository.AuthService
import io.github.lamba92.corpore.app.core.repository.CoilDebugLogger
import io.github.lamba92.corpore.app.core.repository.LoggingService
import io.github.lamba92.corpore.app.core.repository.MockAuthService
import io.github.lamba92.corpore.app.core.repository.StaticLoggingService
import io.github.lamba92.corpore.app.core.usecase.login.LoginWithAppleUseCase
import io.github.lamba92.corpore.app.core.usecase.login.LoginWithGoogleUseCase
import io.github.lamba92.corpore.app.core.usecase.login.LogoutUseCase
import io.github.lamba92.corpore.app.core.utils.CoilLogger
import io.github.lamba92.corpore.app.features.login.LoginScreenViewModel
import io.github.lamba92.corpore.app.features.onboarding.OnboardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

object DiModules {
    val repositories =
        module {
            single<AuthService> { MockAuthService }
            single<CoilLogger> { CoilDebugLogger }
            factory<LoggingService> { (tag: String) -> StaticLoggingService(tag) }
        }

    val useCases =
        module {
            single { LoginWithGoogleUseCase(get()) }
            single { LoginWithAppleUseCase(get()) }
            single { LogoutUseCase(get()) }
        }

    val viewModels =
        module {
            viewModel {
                LoginScreenViewModel(
                    loginWithGoogleUseCase = get(),
                    loginWithAppleUseCase = get(),
                    authRepository = get(),
                    loggingService = get<LoggingService> { parametersOf(LoginScreenViewModel::class.simpleName!!) },
                )
            }
            viewModelOf(::OnboardingViewModel)
        }

    val all = repositories + useCases + viewModels
}
