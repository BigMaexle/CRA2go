package com.example.cra2go.login.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.cra2go.login.domain.model.OAuthLogin
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationManagementActivity
import net.openid.appauth.AuthorizationResponse

class AuthRecievedActivitiy:ComponentActivity() {

    val TAG = "RECIEVED"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(AuthorizationManagementActivity.createResponseHandlingIntent(
            this, intent.data
        ))

        finish()

    }

}