package com.example.cra2go.login.domain.use_cases

data class LoginUseCases(
    val loginToCRAUseCase: LoginToCRAUseCase,
    val saveNewTokenUseCase: SaveNewTokenUseCase
)
