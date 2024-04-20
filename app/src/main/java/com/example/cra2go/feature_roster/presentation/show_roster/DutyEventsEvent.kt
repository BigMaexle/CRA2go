package com.example.cra2go.feature_roster.presentation.show_roster

import com.example.cra2go.feature_roster.domain.model.DutyEvent

sealed class DutyEventsEvent {
    object UpdateRoster :DutyEventsEvent()
    data class DeleteDutyEvent(val dutyevent : DutyEvent) : DutyEventsEvent()

    object DeleteAllEvents : DutyEventsEvent()
}