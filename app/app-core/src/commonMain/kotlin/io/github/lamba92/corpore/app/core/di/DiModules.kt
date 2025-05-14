package io.github.lamba92.corpore.app.core.di

import io.github.lamba92.corpore.app.core.repository.AuthRepository
import io.github.lamba92.corpore.app.core.repository.LoggingRepository
import io.github.lamba92.corpore.app.core.repository.MockAuthRepository
import io.github.lamba92.corpore.app.core.repository.StaticLoggingRepository
import io.github.lamba92.corpore.app.core.usecase.login.LoginWithAppleUseCase
import io.github.lamba92.corpore.app.core.usecase.login.LoginWithGoogleUseCase
import io.github.lamba92.corpore.app.core.usecase.login.LogoutUseCase
import io.github.lamba92.corpore.app.core.viewmodel.LoginScreenViewModel
import io.github.lamba92.corpore.app.core.viewmodel.OnboardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

object DiModules {
    val repositories =
        module {
            single<AuthRepository> { MockAuthRepository }
            factory<LoggingRepository> { (tag: String) -> StaticLoggingRepository(tag) }
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
                    loggingRepository = get<LoggingRepository> { parametersOf(LoginScreenViewModel::class.simpleName!!) },
                )
            }
            viewModelOf(::OnboardingViewModel)
        }

    val all = repositories + useCases + viewModels
}
