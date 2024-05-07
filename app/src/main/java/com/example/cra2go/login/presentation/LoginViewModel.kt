package com.example.cra2go.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cra2go.feature_roster.domain.use_cases.DutyEventUseCases
import com.example.cra2go.feature_roster.presentation.show_roster.DutyEventsEvent
import com.example.cra2go.login.domain.use_cases.LoginUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel  @Inject constructor(
    private val loginUseCases: LoginUseCases
): ViewModel() {


    fun onEvent(event : LoginEvent){
        when (event) {
            is LoginEvent.savenewToken -> {
                viewModelScope.launch { loginUseCases.saveNewTokenUseCase(newToken = event.newToken) }
            }

        }
    }


}