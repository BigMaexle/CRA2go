package com.example.cra2go.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.cra2go.feature_roster.domain.model.DutyEvent
import com.example.cra2go.feature_roster.presentation.show_roster.DutyEventViewModel
import com.example.cra2go.feature_roster.presentation.show_roster.DutyEventsEvent
import kotlinx.coroutines.launch
import net.openid.appauth.TokenResponse

@Composable
fun TokenSave(
    newToken: TokenResponse,
    loginviewModel: LoginViewModel = hiltViewModel(),
    dutyEventViewModel: DutyEventViewModel = hiltViewModel()
)  {


    loginviewModel.onEvent(LoginEvent.savenewToken(newToken))
    if (newToken.accessToken != null) {
        dutyEventViewModel.onEvent(
            DutyEventsEvent.UpdateRoster(
                newToken.accessToken!!,
                LocalContext.current
            )
        )
    }

}