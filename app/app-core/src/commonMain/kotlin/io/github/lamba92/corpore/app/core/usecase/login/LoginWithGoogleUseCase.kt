package io.github.lamba92.corpore.app.core.usecase.login

import io.github.lamba92.corpore.app.core.data.AuthRepository
import io.github.lamba92.corpore.app.core.data.AuthResult
import io.github.lamba92.corpore.app.core.usecase.UseCase

class LoginWithGoogleUseCase(
    private val authRepository: AuthRepository,
) : UseCase<AuthResult, Unit> {
    override suspend fun execute(param: Unit): AuthResult = authRepository.loginWithGoogle()
}

class LoginWithAppleUseCase(
    private val authRepository: AuthRepository,
) : UseCase<AuthResult, Unit> {
    override suspend fun execute(param: Unit): AuthResult = authRepository.loginWithApple()
}