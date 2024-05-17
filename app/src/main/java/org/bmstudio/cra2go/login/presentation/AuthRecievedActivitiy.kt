package org.bmstudio.cra2go.login.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import org.bmstudio.cra2go.login.domain.model.OAuthLogin

@AndroidEntryPoint
class AuthRecievedActivitiy : ComponentActivity() {

    val TAG = "RECIEVED"

    var mAuthlogin: OAuthLogin? = null

    val tokenResponseCallback: AuthorizationService.TokenResponseCallback =
        AuthorizationService.TokenResponseCallback { response, ex ->
            Log.d(TAG, response.toString())
            Log.d(TAG, ex.toString())

            if (response != null) {

                Log.d(TAG, "bin vor setContent")

                setContent { TokenSave(newToken = response) }

                finish()


            }


        }

    var resp: AuthorizationResponse? = null
    var ex: AuthorizationException? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resp = AuthorizationResponse.fromIntent(intent)
        ex = AuthorizationException.fromIntent(intent)
        if (resp != null) {

            mAuthlogin = OAuthLogin(applicationContext)
            mAuthlogin!!.authState.update(resp, ex)


        } else {
            // authorization failed, check ex for more details
        }

    }

    override fun onStart() {
        super.onStart()

        mAuthlogin!!.authService.performTokenRequest(
            resp!!.createTokenExchangeRequest(),
            mAuthlogin!!.clientsecret,
            tokenResponseCallback
        )


    }

}