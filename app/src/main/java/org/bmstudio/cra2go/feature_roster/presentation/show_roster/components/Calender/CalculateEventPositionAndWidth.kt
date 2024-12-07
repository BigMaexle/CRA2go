package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender


import androidx.compose.runtime.Composable
import org.bmstudio.cra2go.feature_roster.domain.model.DisplayEvent
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent

@Composable
fun calculateRotationDuration(startevent: DisplayEvent,endevent:DisplayEvent): Int {

    val duration_ms = endevent.endTime.time - startevent.startTime.time

    return duration_ms.toInt()
}
