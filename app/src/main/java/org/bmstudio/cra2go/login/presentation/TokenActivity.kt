/*
 * Copyright 2015 The AppAuth for Android Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bmstudio.cra2go.login.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationService.TokenResponseCallback
import net.openid.appauth.AuthorizationServiceDiscovery
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientAuthentication.UnsupportedAuthenticationMethod
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenResponse
import okio.Okio
import org.bmstudio.cra2go.login.domain.model.AuthStateManager
import org.bmstudio.cra2go.login.domain.model.Configuration
import org.bmstudio.cra2go.login.presentation.LoginActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.nio.charset.Charset
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference

/**
 * Displays the authorized state of the user. This activity is provided with the outcome of the
 * authorization flow, which it uses to negotiate the final authorized state,
 * by performing an authorization code exchange if necessary. After this, the activity provides
 * additional post-authorization operations if available, such as fetching user info and refreshing
 * access tokens.
 */
class TokenActivity : AppCompatActivity() {

    private val TAG: String = "TokenActivity"

    lateinit var mAuthService: AuthorizationService
    lateinit var mConfiguration: Configuration

    lateinit var mStateManager: AuthStateManager
    private val mUserInfoJson = AtomicReference<JSONObject?>()
    private var mExecutor: ExecutorService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG,"Creating Token Activity")

        mStateManager = AuthStateManager.getInstance(this)
        mExecutor = Executors.newSingleThreadExecutor()
        mConfiguration = Configuration.getInstance(this)

        val config: Configuration = Configuration.getInstance(this)


        if (config.hasConfigurationChanged()) {
            Toast.makeText(
                this,
                "Configuration change detected",
                Toast.LENGTH_SHORT
            )
                .show()
            //signOut()
            //return
        }

        mAuthService = AuthorizationService(
            this,
            AppAuthConfiguration.Builder()
                .setConnectionBuilder(config.getConnectionBuilder())
                .build()
        )

        if (savedInstanceState != null) {
            try {
                mUserInfoJson.set(JSONObject(savedInstanceState.getString(KEY_USER_INFO)!!))
            } catch (ex: JSONException) {
                Log.e(TAG, "Failed to parse saved user info JSON, discarding", ex)
            }
        }
    }

    override fun onStart() {
        super.onStart()


        if (mExecutor!!.isShutdown) {
            mExecutor = Executors.newSingleThreadExecutor()
        }
        if (mStateManager.getCurrent().isAuthorized) {
            Log.i(TAG,"Already Authorized, skipping Auth Token")
            finish()
            return
        }

        // the stored AuthState is incomplete, so check if we are currently receiving the result of
        // the authorization flow from the browser.
        val response = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)
        if (response != null || ex != null) {
            mStateManager.updateAfterAuthorization(response, ex)
        }
        if (response?.authorizationCode != null) {
            // authorization code exchange is required
            mStateManager.updateAfterAuthorization(response, ex)
            exchangeAuthorizationCode(response)
        } else if (ex != null) {
            Log.w(TAG,"Authorization flow failed: " + ex.message)
        } else {
            Log.w(TAG,"No authorization state retained - reauthorization required")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // user info is retained to survive activity restarts, such as when rotating the
        // device or switching apps. This isn't essential, but it helps provide a less
        // jarring UX when these events occur - data does not just disappear from the view.
        if (mUserInfoJson.get() != null) {
            outState.putString(KEY_USER_INFO, mUserInfoJson.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAuthService?.dispose()
        mExecutor!!.shutdownNow()
    }




    @MainThread
    private fun refreshAccessToken() {
        performTokenRequest(
            mStateManager.getCurrent().createTokenRefreshRequest()
        ) { tokenResponse: TokenResponse?, authException: AuthorizationException? ->
            handleAccessTokenResponse(
                tokenResponse,
                authException
            )
        }
    }

    @MainThread
    private fun exchangeAuthorizationCode(authorizationResponse: AuthorizationResponse) {
        Log.i(TAG,"Exchange Authorization Token")
        performTokenRequest(
            authorizationResponse.createTokenExchangeRequest()
        ) { tokenResponse: TokenResponse?, authException: AuthorizationException? ->
            handleCodeExchangeResponse(
                tokenResponse,
                authException
            )
        }
    }

    @MainThread
    private fun performTokenRequest(
        request: TokenRequest,
        callback: TokenResponseCallback
    ) {
        val clientAuthentication: ClientAuthentication
        clientAuthentication = try {
            mStateManager.getCurrent().getClientAuthentication()
        } catch (ex: UnsupportedAuthenticationMethod) {
            Log.d(
                TAG, "Token request cannot be made, client authentication for the token "
                        + "endpoint could not be constructed (%s)", ex
            )
            Log.e(TAG,"Client authentication method is unsupported")
            return
        }
        mAuthService.performTokenRequest(
            request,
            clientAuthentication,
            callback
        )
    }

    @WorkerThread
    private fun handleAccessTokenResponse(
        tokenResponse: TokenResponse?,
        authException: AuthorizationException?
    ) {
        mStateManager.updateAfterTokenResponse(tokenResponse, authException)
    }

    @WorkerThread
    private fun handleCodeExchangeResponse(
        tokenResponse: TokenResponse?,
        authException: AuthorizationException?
    ) {
        mStateManager.updateAfterTokenResponse(tokenResponse, authException)
        if (!mStateManager.getCurrent().isAuthorized()) {
            val message = ("Authorization Code exchange failed "
                    + if (authException != null) authException.error else "")

            // WrongThread inference is incorrect for lambdas
            runOnUiThread { Log.w(TAG,message)}
        } else {
            Log.i(TAG,"Exchange Sucessfull")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == END_SESSION_REQUEST_CODE && resultCode == RESULT_OK) {
            signOut()
            finish()
        } else {

        }
    }



    @MainThread
    private fun signOut() {
        // discard the authorization and token state, but retain the configuration and
        // dynamic client registration (if applicable), to save from retrieving them again.
        val currentState: AuthState = mStateManager.getCurrent()
        val clearedState = AuthState(currentState.getAuthorizationServiceConfiguration()!!)
        if (currentState.lastRegistrationResponse != null) {
            clearedState.update(currentState.lastRegistrationResponse)
        }
        mStateManager.replace(clearedState)

        finish()
    }

    companion object {
        private const val TAG = "TokenActivity"
        private const val KEY_USER_INFO = "userInfo"
        private const val END_SESSION_REQUEST_CODE = 911
    }
}
