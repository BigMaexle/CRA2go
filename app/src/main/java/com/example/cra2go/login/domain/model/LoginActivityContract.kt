package com.example.cra2go.login.domain.model

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.browser.BrowserAllowList
import net.openid.appauth.browser.VersionedBrowserMatcher


class OAuthLogin (context: Context){

    val serviceConfig = AuthorizationServiceConfiguration(
    Uri.parse("https://oauth-test.lufthansa.com/lhcrew/oauth/authorize"),  // authorization endpoint
    Uri.parse("https://oauth-test.lufthansa.com/lhcrew/oauth/token")
    ) // token endpoint

    val clientsecret: ClientAuthentication = ClientSecretBasic("rpgtUArDew")

    val MY_CLIENT_ID: String = "st53sq4qnbrvbbbtedgqk3dc"

    val MY_REDIRECT_URI = Uri.parse("https://com.example.cra2go")

    val authRequestBuilder = AuthorizationRequest.Builder(
        serviceConfig,  // the authorization service configuration
        MY_CLIENT_ID,  // the client ID, typically pre-registered and static
        ResponseTypeValues.CODE,  // the response_type value: we want a code
        MY_REDIRECT_URI
    ) // the redirect URI to which the auth response is sent

    val authRequest = authRequestBuilder
        .setScope("https://mock.cms.fra.dlh.de/publicCrewApiDev")
        .build()

    var authState = AuthState(serviceConfig)

    val appAuthConfig = AppAuthConfiguration.Builder()
        .setBrowserMatcher(BrowserAllowList(
                VersionedBrowserMatcher.CHROME_BROWSER,
                VersionedBrowserMatcher.SAMSUNG_BROWSER))
        .build()

    val authService : AuthorizationService = AuthorizationService(context,appAuthConfig)
    val authIntent : Intent = authService.getAuthorizationRequestIntent(authRequest)


}

class OAuthContract(
    val authintent: Intent) : ActivityResultContract<String,String>(){
    override fun createIntent(context: Context, input: String): Intent {
        return authintent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String {return "" }

}
