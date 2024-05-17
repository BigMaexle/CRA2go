package org.bmstudio.cra2go.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.bmstudio.cra2go.login.domain.use_cases.LoginUseCases
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
) : ViewModel() {


    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.savenewToken -> {
                viewModelScope.launch { loginUseCases.saveNewTokenUseCase(newToken = event.newToken) }
            }

        }
    }


}