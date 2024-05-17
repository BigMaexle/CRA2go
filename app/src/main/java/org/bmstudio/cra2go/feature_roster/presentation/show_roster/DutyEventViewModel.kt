package org.bmstudio.cra2go.feature_roster.presentation.show_roster

import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.bmstudio.cra2go.feature_roster.domain.use_cases.DutyEventUseCases
import org.bmstudio.cra2go.login.domain.use_cases.LoginUseCases
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DutyEventViewModel @Inject constructor(
    private val dutyEventUseCases: DutyEventUseCases,
    private val loginUseCases: LoginUseCases
) : ViewModel() {

    private val _state = mutableStateOf(DutyEventsStates())
    val state: State<DutyEventsStates> = _state

    private val _authorizationRequest = MutableLiveData<Intent>()
    val authorizationRequest: LiveData<Intent> = _authorizationRequest

    private var getNotesJob: Job? = null


    init {
        getEvents()
    }


    fun onEvent(event: DutyEventsEvent) {
        when (event) {
            is DutyEventsEvent.UpdateRoster -> {
                viewModelScope.launch {
                    dutyEventUseCases.updateRoster(
                        event.token,
                        Date()
                    )
                }
            }

            is DutyEventsEvent.DeleteDutyEvent -> {
                viewModelScope.launch { dutyEventUseCases.deleteDutyEvent(event.dutyevent) }
            }

            is DutyEventsEvent.DeleteAllEvents -> {

                viewModelScope.launch {
                    state.value.events.onEach { dutyEvent ->
                        dutyEventUseCases.deleteDutyEvent(dutyEvent)
                    }
                }
            }
        }
    }

    private fun getEvents() {
        getNotesJob?.cancel()
        getNotesJob = dutyEventUseCases.getDutyEvents()
            .onEach { events ->
                _state.value = state.value.copy(
                    events = events,
                )
            }.launchIn(viewModelScope)
    }

}