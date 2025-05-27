package io.github.lamba92.corpore.server.usecase

import io.github.lamba92.corpore.server.auth.AppleTokenVerifier
import io.github.lamba92.corpore.server.data.TokenRepository
import io.github.lamba92.corpore.server.data.UserRepository

class RegisterOrLoginUserWithAppleUseCase(
    userRepository: UserRepository,
    tokenRepository: TokenRepository,
    verifier: AppleTokenVerifier,
) : RegisterOrLoginUserUseCase(
        userRepository = userRepository,
        tokenRepository = tokenRepository,
        verifier = verifier,
    )
