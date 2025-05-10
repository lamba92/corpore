package io.github.lamba92.corpore.app.core.di

import io.github.lamba92.corpore.app.core.repository.AuthRepository
import io.github.lamba92.corpore.app.core.repository.LoggingRepository
import io.github.lamba92.corpore.app.core.repository.MockAuthRepository
import io.github.lamba92.corpore.app.core.repository.StaticLoggingRepository
import io.github.lamba92.corpore.app.core.usecase.login.LoginWithAppleUseCase
import io.github.lamba92.corpore.app.core.usecase.login.LoginWithGoogleUseCase
import io.github.lamba92.corpore.app.core.viewmodel.LoginScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object DiModules {
    val repositories =
        module {
            single<AuthRepository> { MockAuthRepository }
            single<LoggingRepository> { StaticLoggingRepository }
        }
    val useCases =
        module {
            single { LoginWithGoogleUseCase(get()) }
            single { LoginWithAppleUseCase(get()) }
        }
    val viewModels =
        module {
            viewModelOf(::LoginScreenViewModel)
        }

    val all = repositories + useCases + viewModels
}
