package com.example.cra2go.feature_roster.presentation.show_roster

import android.content.Context
import com.example.cra2go.feature_roster.domain.model.DutyEvent

sealed class DutyEventsEvent {
    data class UpdateRoster (val token: String, val context: Context) :DutyEventsEvent()
    data class DeleteDutyEvent(val dutyevent : DutyEvent) : DutyEventsEvent()

    object DeleteAllEvents : DutyEventsEvent()
}