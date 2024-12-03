package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import java.util.Calendar
import java.util.Date

@Composable
fun calculateRotationDuration(startevent: DutyEvent,endevent:DutyEvent): Int {

    if (startevent.startTime == null) return 0
    if (endevent.endTime == null) return 0
    if (startevent.wholeDay == true) return 0

    val duration_ms = endevent.endTime.time - startevent.startTime.time

    return duration_ms.toInt()
}
