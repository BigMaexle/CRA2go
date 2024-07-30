package org.bmstudio.cra2go.login.presentation

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.Text
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.browser.AnyBrowserMatcher
import net.openid.appauth.browser.BrowserMatcher
import org.bmstudio.cra2go.login.domain.model.AuthStateManager
import org.bmstudio.cra2go.login.domain.model.Configuration
import org.bmstudio.cra2go.login.domain.model.OAuthLogin
import org.bmstudio.cra2go.ui.theme.CRA2goTheme
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference


@AndroidEntryPoint
class LoginActivity() : ComponentActivity() {

    private val TAG: String = "LoginActivity"
    private val EXTRA_FAILED: String = "failed"
    private val RC_AUTH: Int = 100


    lateinit var mAuthService: AuthorizationService
    lateinit var mAuthStateManager: AuthStateManager
    lateinit var mConfiguration: Configuration

    private val mClientId: AtomicReference<String> = AtomicReference<String>()
    private val mAuthRequest: AtomicReference<AuthorizationRequest> = AtomicReference<AuthorizationRequest>()
    private val mAuthIntent: AtomicReference<CustomTabsIntent> = AtomicReference<CustomTabsIntent>()

    private var mAuthIntentLatch: CountDownLatch = CountDownLatch(1)

    lateinit var mExecutor: Executor

    private var mBrowserMatcher: BrowserMatcher = AnyBrowserMatcher.INSTANCE


    private var oauthlogin: OAuthLogin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mExecutor = Executors.newSingleThreadExecutor()
        mAuthStateManager = AuthStateManager.getInstance(this)
        mConfiguration = Configuration.getInstance(this)
        mAuthService = createAuthorizationService()

        if (mAuthStateManager.current.isAuthorized
            && !mConfiguration.hasConfigurationChanged()
        ) {
            Log.i(TAG, "User is already authenticated, proceeding to token activity")
            //startActivity(Intent(this, TokenActivity::class.java))
            finish()
            return
        }

        if (!mConfiguration.isValid) {
            Log.e(TAG,mConfiguration.configurationError.toString())
            //return
        }


        mExecutor.execute( {
            this.initializeAppAuth()
            this.createAuthorizationService()
            this.createAuthRequest("")
            this.startAuth()
        } )






        setContent {
            CRA2goTheme {
                Text(text = "LOGINACTIVITY")
            }
        }


        //getContent.launch("10")
    }

    override fun onDestroy() {
        super.onDestroy()

        if (mAuthService != null) {
            mAuthService.dispose()
        }
    }

    override fun onStart() {
        super.onStart()

        finish()

    }

    private fun warmUpBrowser() {
        mAuthIntentLatch = CountDownLatch(1)
        mExecutor.execute {
            Log.i(TAG, "Warming up browser instance for auth request")
            val intentBuilder =
                mAuthService.createCustomTabsIntentBuilder(mAuthRequest.get().toUri())
            mAuthIntent.set(intentBuilder.build())
            mAuthIntentLatch.countDown()
        }
    }


    @WorkerThread
    private fun initializeAppAuth() {
        Log.i(TAG, "Initializing AppAuth")
        recreateAuthorizationService()

        if (mAuthStateManager.current.authorizationServiceConfiguration != null) {
            // configuration is already created, skip to client initialization
            Log.i(TAG, "auth config already established")
            warmUpBrowser()
            initializeClient()
            return
        }

        // if we are not using discovery, build the authorization service configuration directly
        // from the static configuration values.
        if (mConfiguration.discoveryUri == null) {
            Log.i(TAG, "Creating auth config from res/raw/auth_config.json")
            val config = AuthorizationServiceConfiguration(
                mConfiguration.authEndpointUri!!,
                mConfiguration.tokenEndpointUri!!,
                mConfiguration.registrationEndpointUri,
                mConfiguration.endSessionEndpoint
            )

            mAuthStateManager.replace(AuthState(config))

            warmUpBrowser()
            initializeClient()


            return
        }

    }

    private fun recreateAuthorizationService() {
        if (mAuthService != null) {
            Log.i(TAG, "Discarding existing AuthService instance")
            mAuthService.dispose()
        }
        mAuthService = createAuthorizationService()
        mAuthRequest.set(null)
        mAuthIntent.set(null)
    }

    private fun createAuthorizationService(): AuthorizationService {
        Log.i(TAG, "Creating authorization service")
        val builder = AppAuthConfiguration.Builder()
        builder.setBrowserMatcher(mBrowserMatcher)
        builder.setConnectionBuilder(mConfiguration.connectionBuilder)

        return AuthorizationService(this, builder.build())
    }

    @WorkerThread
    private fun initializeClient() {
        if (mConfiguration.clientId != null) {
            Log.i(TAG, "Using static client ID: " + mConfiguration.clientId)
            // use a statically configured client ID
            mClientId.set(mConfiguration.clientId)
            runOnUiThread { this.initializeAuthRequest() }
            return
        }

    }

    @MainThread
    private fun initializeAuthRequest() {
        createAuthRequest("")
    }

    private fun createAuthRequest(loginHint: String?) {
        Log.i(TAG, "Creating auth request for login hint: $loginHint")
        val authRequestBuilder = AuthorizationRequest.Builder(
            mAuthStateManager.current.authorizationServiceConfiguration!!,
            mClientId.get(),
            ResponseTypeValues.CODE,
            mConfiguration.redirectUri
        )
            .setScope(mConfiguration.scope)

        if (!TextUtils.isEmpty(loginHint)) {
            authRequestBuilder.setLoginHint(loginHint)
        }

        mAuthRequest.set(authRequestBuilder.build())

    }

    @MainThread
    fun startAuth() {
        Log.i(TAG,"Making authorization request")

        // WrongThread inference is incorrect for lambdas
        // noinspection WrongThread
        mExecutor.execute(Runnable { this.doAuth() })
    }

    @WorkerThread
    private fun doAuth() {

        val completionIntent = Intent(
            this,
            AuthRecievedActivitiy::class.java)
        val cancelIntent = Intent(
            this,
            LoginActivity::class.java)
        cancelIntent.putExtra(EXTRA_FAILED, true)
        cancelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var flags = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags = flags or PendingIntent.FLAG_MUTABLE
        }

        mAuthService.performAuthorizationRequest(
            mAuthRequest.get(),
            PendingIntent.getActivity(this, 0, completionIntent, flags),
            PendingIntent.getActivity(this, 0, cancelIntent, flags),
            mAuthIntent.get())
    }

}



