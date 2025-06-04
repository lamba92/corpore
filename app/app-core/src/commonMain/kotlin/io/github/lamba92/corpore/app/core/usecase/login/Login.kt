package io.github.lamba92.corpore.app.core.usecase.login

import io.github.lamba92.corpore.app.core.repository.AuthRepository
import io.github.lamba92.corpore.app.core.repository.AuthResult
import io.github.lamba92.corpore.common.core.usecase.FunctionalUseCase

class LoginWithGoogleUseCase(
    private val authRepository: AuthRepository,
) : FunctionalUseCase<AuthResult, Unit> {
    override suspend fun execute(param: Unit): AuthResult = authRepository.loginWithGoogle()
}

class LoginWithAppleUseCase(
    private val authRepository: AuthRepository,
) : FunctionalUseCase<AuthResult, Unit> {
    override suspend fun execute(param: Unit): AuthResult = authRepository.loginWithApple()
}

class LogoutUseCase(
    private val authRepository: AuthRepository,
) : FunctionalUseCase<Unit, Unit> {
    override suspend fun execute(param: Unit) = authRepository.logout()
}
