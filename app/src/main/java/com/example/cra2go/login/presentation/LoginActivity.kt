package com.example.cra2go.login.presentation

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cra2go.CRA2go_GeneratedInjector
import com.example.cra2go.feature_roster.presentation.show_roster.DutyEventViewModel
import com.example.cra2go.login.domain.model.OAuthLogin
import com.example.cra2go.ui.theme.CRA2goTheme
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService

@AndroidEntryPoint
class LoginActivity (
    private var oauthlogin: OAuthLogin? = null,
    private val TAG: String = "LoginActivity",
): ComponentActivity(){




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val resp = AuthorizationResponse.fromIntent(data!!)
        val ex = AuthorizationException.fromIntent(data)

        continueWithTokenRequest(resp,ex)

    }

    private fun continueWithTokenRequest(
        resp: AuthorizationResponse?,
        ex: AuthorizationException?
    ) {

        oauthlogin?.authState?.update(resp,ex)


        val tokenResponseCallback : AuthorizationService.TokenResponseCallback = AuthorizationService.TokenResponseCallback { response, ex ->
            Log.d(TAG, response.toString())
            Log.d(TAG, ex.toString())

            setContent {
                if (response != null) {
                    TokenSave(newToken = response)
                }
            }

            finish()


        }

        if (oauthlogin == null) {
            return
        }
        oauthlogin!!.authService.performTokenRequest(
            resp!!.createTokenExchangeRequest(),
            oauthlogin!!.clientsecret,
            tokenResponseCallback
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.action == "AUTH"){
            oauthlogin = OAuthLogin(applicationContext)
            startActivityForResult(oauthlogin!!.authIntent, 0)
        }




        setContent {
            CRA2goTheme {
                Text(text = "LOGINACTIVITY")
            }
        }


        //getContent.launch("10")
    }


}