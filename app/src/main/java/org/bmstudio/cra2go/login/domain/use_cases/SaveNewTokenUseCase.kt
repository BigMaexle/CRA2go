package org.bmstudio.cra2go.login.domain.use_cases

import android.util.Log
import net.openid.appauth.TokenResponse
import org.bmstudio.cra2go.login.domain.repository.AccessTokenRepository

class SaveNewTokenUseCase(
    private val accessTokenRepository: AccessTokenRepository
) {

    suspend operator fun invoke(newToken: TokenResponse) {

        Log.d("SAVEUSECASE", "HABE NEUEN TOKEN")

        accessTokenRepository.savenewAcessToken(newToken)

    }

}