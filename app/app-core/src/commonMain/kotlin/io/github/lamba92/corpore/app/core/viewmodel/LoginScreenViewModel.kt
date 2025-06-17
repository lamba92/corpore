package io.github.lamba92.corpore.app.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lamba92.corpore.app.core.repository.AuthRepository
import io.github.lamba92.corpore.app.core.repository.AuthResult
import io.github.lamba92.corpore.app.core.repository.LoggingService
import io.github.lamba92.corpore.app.core.repository.logError
import io.github.lamba92.corpore.app.core.usecase.execute
import io.github.lamba92.corpore.app.core.usecase.login.LoginWithAppleUseCase
import io.github.lamba92.corpore.app.core.usecase.login.LoginWithGoogleUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginWithAppleUseCase: LoginWithAppleUseCase,
    private val loggingService: LoggingService,
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

    private val errorsChannel = Channel<Unit>(onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val errorsFlow =
        errorsChannel
            .consumeAsFlow()
            .shareIn(viewModelScope, SharingStarted.Eagerly, 0)

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
        loggingService.logInfo("Login started")
        stateFlow.value = true
        viewModelScope.launch {
            val result = action()
            if (result is AuthResult.Failure) {
                errorsChannel.send(Unit)
                loggingService.logError(result.error)
            }
            stateFlow.value = false
            loggingService.logInfo("Login finished")
        }
    }
}
