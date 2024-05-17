package org.bmstudio.cra2go.feature_roster.presentation.show_roster

import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent

data class DutyEventsStates(
    val events: List<DutyEvent> = emptyList()
)