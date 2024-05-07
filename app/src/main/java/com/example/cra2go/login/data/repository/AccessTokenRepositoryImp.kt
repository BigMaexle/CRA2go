package com.example.cra2go.login.data.repository

import com.example.cra2go.login.domain.repository.AccessTokenRepository
import net.openid.appauth.TokenResponse

class AccessTokenRepositoryImp(
    private var accessToken: TokenResponse?
): AccessTokenRepository {
    override suspend fun getAccessToken(): TokenResponse {
        return accessToken!!
    }

    override suspend fun checkforvalidAccessToken(): Boolean {
        return if (accessToken != null) {
            true
        } else {
            false
        }
    }

    override suspend fun savenewAcessToken(newToken: TokenResponse) {
        accessToken = newToken
    }
}