package com.example.cra2go.login.domain.use_cases

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.cra2go.login.domain.repository.AccessTokenRepository
import com.example.cra2go.login.presentation.LoginActivity


class LoginToCRAUseCase (
    private val accessTokenRepository: AccessTokenRepository
){

    operator fun invoke(context: Context){


    }


}