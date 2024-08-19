package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import java.util.Calendar
import java.util.Date

@Composable
fun calculateEventPositionAndWidth(startevent: DutyEvent,endevent:DutyEvent, cellWidth: Dp): Pair<Dp, Dp> {

    if (startevent.startTime == null) return Pair(0.dp,cellWidth)
    if (endevent.endTime == null) return Pair(0.dp,cellWidth)
    if (startevent.wholeDay == true) return Pair(0.dp,cellWidth)


    // Calculate the start position as a fraction of the 24-hour day
    val startHourFraction = (startevent.startTime.hours + (startevent.startTime.minutes / 60f) ) / 24f

    val duration_ms = endevent.endTime.time - startevent.startTime.time
    val duration_in_days: Float = 1f * duration_ms / 86400000f
    val duration_in_dp = cellWidth * duration_in_days
    val eventWidth = duration_in_dp



    // Convert back to Dp for usage in Composable
    val startPosition = cellWidth * startHourFraction

    return Pair(startPosition, eventWidth)
}
