package io.github.lamba92.corpore.server.usecase

import io.github.lamba92.corpore.server.auth.GoogleTokenVerifier
import io.github.lamba92.corpore.server.data.TokenRepository
import io.github.lamba92.corpore.server.data.UserRepository

class RegisterOrLoginUserWithGoogleUseCase(
    userRepository: UserRepository,
    tokenRepository: TokenRepository,
    verifier: GoogleTokenVerifier,
) : RegisterOrLoginUserUseCase(
        userRepository = userRepository,
        tokenRepository = tokenRepository,
        verifier = verifier,
    )
