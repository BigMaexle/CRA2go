package com.example.cra2go.feature_roster.presentation.show_roster

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cra2go.feature_roster.domain.model.DutyEvent
import com.example.cra2go.feature_roster.domain.use_cases.DutyEventUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DutyEventViewModel @Inject constructor(
    private val dutyEventUseCases : DutyEventUseCases
) : ViewModel(){

    private val _state = mutableStateOf(DutyEventsStates())
    val state: State<DutyEventsStates> = _state

    private var getNotesJob: Job? = null

    init {
        getEvents()
    }

  fun onEvent(event : DutyEventsEvent){
        when (event) {
            is DutyEventsEvent.UpdateRoster -> {
                viewModelScope.launch { dutyEventUseCases.updateDutyEventsFromCRA() }
            }
            is DutyEventsEvent.DeleteDutyEvent -> {
                viewModelScope.launch { dutyEventUseCases.deleteDutyEvent(event.dutyevent) }
            }

            is DutyEventsEvent.DeleteAllEvents -> {

                viewModelScope.launch{
                    state.value.events.onEach {
                            dutyEvent -> dutyEventUseCases.deleteDutyEvent(dutyEvent)
                    }
                }
            }
        }
    }

    private fun getEvents(){
        getNotesJob?.cancel()
        getNotesJob = dutyEventUseCases.getDutyEvents()
            .onEach { events->
                _state.value = state.value.copy(
                    events = events,
                )
            }.launchIn(viewModelScope)
    }

}