package org.bmstudio.cra2go.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import net.openid.appauth.TokenResponse
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.DutyEventViewModel
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.DutyEventsEvent

@Composable
fun TokenSave(
    newToken: TokenResponse,
    loginviewModel: LoginViewModel = hiltViewModel(),
    dutyEventViewModel: DutyEventViewModel = hiltViewModel()
) {


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