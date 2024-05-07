package com.example.cra2go.login.domain.repository

import net.openid.appauth.TokenResponse

interface AccessTokenRepository {

    suspend fun getAccessToken(): TokenResponse

    suspend fun checkforvalidAccessToken(): Boolean

    suspend fun savenewAcessToken(newToken: TokenResponse)
}