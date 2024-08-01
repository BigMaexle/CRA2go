package org.bmstudio.cra2go.feature_roster.domain.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import org.bmstudio.cra2go.login.presentation.LoginActivity


class LoginActivityContract : ActivityResultContract<Int, String?>() {

    override fun createIntent(context: Context, input: Int): Intent {
        return Intent(context, LoginActivity::class.java).apply {
            putExtra("Test", 100)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        val data = intent?.getStringExtra("AccessCode")
        return if (resultCode == Activity.RESULT_OK && data != null) data
        else null
    }
}