package org.bmstudio.cra2go.login.presentation


import net.openid.appauth.TokenResponse

sealed class LoginEvent {

    data class savenewToken(val newToken: TokenResponse) : LoginEvent()

}