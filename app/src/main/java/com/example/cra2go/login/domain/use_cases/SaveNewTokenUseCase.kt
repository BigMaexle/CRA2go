package com.example.cra2go.login.domain.use_cases

import android.util.Log
import com.example.cra2go.login.domain.repository.AccessTokenRepository
import net.openid.appauth.TokenResponse

class SaveNewTokenUseCase(
    private val accessTokenRepository: AccessTokenRepository
) {

    suspend operator fun invoke(newToken: TokenResponse){

        Log.d("SAVEUSECASE","HABE NEUEN TOKEN")

        accessTokenRepository.savenewAcessToken(newToken)

    }

}