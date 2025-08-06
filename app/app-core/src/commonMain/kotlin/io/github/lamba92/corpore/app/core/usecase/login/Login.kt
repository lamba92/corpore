package io.github.lamba92.corpore.app.core.usecase.login

import io.github.lamba92.corpore.app.core.repository.AuthResult
import io.github.lamba92.corpore.app.core.repository.AuthService
import io.github.lamba92.corpore.common.core.usecase.FunctionalUseCase

class LoginWithGoogleUseCase(
    private val authRepository: AuthService,
) : FunctionalUseCase<Unit, AuthResult> {
    override suspend fun execute(param: Unit): AuthResult = authRepository.loginWithGoogle()
}

class LoginWithAppleUseCase(
    private val authRepository: AuthService,
) : FunctionalUseCase<Unit, AuthResult> {
    override suspend fun execute(param: Unit): AuthResult = authRepository.loginWithApple()
}

class LogoutUseCase(
    private val authRepository: AuthService,
) : FunctionalUseCase<Unit, Unit> {
    override suspend fun execute(param: Unit) = authRepository.logout()
}
