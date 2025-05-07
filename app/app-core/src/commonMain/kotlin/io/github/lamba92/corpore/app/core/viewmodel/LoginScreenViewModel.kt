package io.github.lamba92.corpore.app.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lamba92.corpore.app.core.data.AuthRepository
import io.github.lamba92.corpore.app.core.data.AuthResult
import io.github.lamba92.corpore.app.core.data.LoggingRepository
import io.github.lamba92.corpore.app.core.data.logError
import io.github.lamba92.corpore.app.core.usecase.login.LoginWithAppleUseCase
import io.github.lamba92.corpore.app.core.usecase.login.LoginWithGoogleUseCase
import io.github.lamba92.corpore.app.core.usecase.execute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginWithAppleUseCase: LoginWithAppleUseCase,
    private val loggingRepository: LoggingRepository,
    authRepository: AuthRepository,
) : ViewModel() {
    val isLoggedInStateFlow =
        authRepository
            .authSession
            .map { it != null }
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isLoggingInUsingGoogleStateFlow =
        MutableStateFlow(false)

    val isLoggingInUsingAppleStateFlow =
        MutableStateFlow(false)

    val isErrorStateFlow =
        MutableStateFlow(false)

    fun loginWithGoogle() {
        login(isLoggingInUsingGoogleStateFlow, loginWithGoogleUseCase::execute)
    }

    fun loginWithApple() {
        login(isLoggingInUsingAppleStateFlow, loginWithAppleUseCase::execute)
    }

    fun onTOSClicked() {
        // TODO navigate to TOS
    }

    private fun login(
        stateFlow: MutableStateFlow<Boolean>,
        action: suspend () -> AuthResult,
    ) {
        stateFlow.value = true
        viewModelScope.launch {
            val result = action()
            if (result is AuthResult.Failure) {
                isErrorStateFlow.value = true
                loggingRepository.logError(result.error)
            }
            stateFlow.value = false
        }
    }
}
