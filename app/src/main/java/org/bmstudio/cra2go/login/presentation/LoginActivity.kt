package org.bmstudio.cra2go.login.presentation

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import dagger.hilt.android.AndroidEntryPoint
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.MainActivity
import org.bmstudio.cra2go.login.domain.model.OAuthLogin
import org.bmstudio.cra2go.ui.theme.CRA2goTheme

@AndroidEntryPoint
class LoginActivity(
    private var oauthlogin: OAuthLogin? = null,
    private val TAG: String = "LoginActivity",
) : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (intent.action == "AUTH") {
            oauthlogin = OAuthLogin(applicationContext)
            finish()
            oauthlogin!!.authService.performAuthorizationRequest(
                oauthlogin!!.authRequest,
                PendingIntent.getActivity(
                    applicationContext,
                    0,
                    Intent(applicationContext, AuthRecievedActivitiy::class.java),
                    PendingIntent.FLAG_MUTABLE
                ),
                PendingIntent.getActivity(
                    applicationContext,
                    0,
                    Intent(applicationContext, MainActivity::class.java),
                    PendingIntent.FLAG_MUTABLE
                )
            )
        }




        setContent {
            CRA2goTheme {
                Text(text = "LOGINACTIVITY")
            }
        }


        //getContent.launch("10")
    }


}