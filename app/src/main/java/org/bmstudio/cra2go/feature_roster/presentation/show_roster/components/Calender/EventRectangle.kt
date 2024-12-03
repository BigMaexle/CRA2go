package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.TestEvents.TestEvents
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun EventRectangle( events: List<DutyEvent>, cellWidth: Dp) {

    val TAG = "EventRectangle"

    val earliestEvent = earliestEventWithValidTime(events)
    val latestEvent = latestEventWithValidTime(events)

    if (earliestEvent == null || latestEvent == null) {

        if (events.size == 1 && events.first().wholeDay == true){
            Box(
                modifier = Modifier
                    .requiredWidth(cellWidth)
                    .height(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .border(BorderStroke(0.2.dp, Color.Black), RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .zIndex(1.0f),)
        }

        return
    }


    val eventDuration: Int = calculateRotationDuration(
        startevent = earliestEvent,
        endevent = latestEvent
    )



    val time_to_sunday_evening: Int = calculateTimeUntilSundayEvening(earliestEvent)

    val displayable_rotation_lenth: Int = kotlin.math.min(time_to_sunday_evening, eventDuration)

    val eventWidth: Dp = convert_ms_to_dp(displayable_rotation_lenth.toLong(), cellWidth)

    val time_of_day_of_first_event: Float = earliestEvent.startTime!!.hours + earliestEvent.startTime.minutes / 60f
    val startPosition: Dp = convert_ms_to_dp((time_of_day_of_first_event * 60 * 60 * 1000).toLong(), cellWidth)

    val exceeding_length: Dp = determine_exceeding_length(eventWidth, cellWidth)

    Box(
        modifier = Modifier
            .absoluteOffset(x = startPosition + (exceeding_length * 0.5f))
            .requiredWidth(eventWidth)
            .height(10.dp)
            .clip(RoundedCornerShape(2.dp))
            .border(BorderStroke(0.2.dp, Color.Black), RoundedCornerShape(2.dp))
            .zIndex(1.0f),
        contentAlignment = Alignment.CenterStart

    )
    {

        FillRotationBox(
            starttime = earliestEvent.startTime,
            endtime = latestEvent.endTime!!,
            events = events,
            cellWidth = cellWidth
        )

    }
}

fun determine_exceeding_length(eventWidth: Dp, cellWidth: Dp): Dp {
    if (eventWidth > cellWidth) {
        return (eventWidth - cellWidth)
    } else {
        return 0.dp
    }
}



fun calculateTimeUntilSundayEvening(startEvent: DutyEvent): Int {
    if (startEvent.startTime == null) return 0

    val cal_startEvent = Calendar.getInstance().apply { time = startEvent.startTime }

    if (cal_startEvent.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
        cal_startEvent.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        if (cal_startEvent.time.time < startEvent.startTime.time) {
            cal_startEvent.add(Calendar.DAY_OF_MONTH, 7)
        }
    }

    cal_startEvent.set(Calendar.HOUR_OF_DAY,23)
    cal_startEvent.set(Calendar.MINUTE,59)
    cal_startEvent.set(Calendar.SECOND,59)

    return (cal_startEvent.time.time - startEvent.startTime.time).toInt()

}

@Composable
fun FillRotationBox(starttime: Date, endtime: Date, events: List<DutyEvent>, cellWidth: Dp) {

    val TAG = "FillRotationBox"

    val flight_events = events.filter {
        it.startTime != null && it.endTime != null }.sortedBy { it.startTime }

    flight_events.forEach { event ->

        //calculate offset of beginning of rotationbox in .dp
        val offset_inside_rot_box: Dp = convert_ms_to_dp(event.startTime!!.time - starttime.time, cellWidth)

        val eventWidth: Dp = convert_ms_to_dp(event.endTime!!.time - event.startTime!!.time, cellWidth)

        if (event.eventCategory != "dummy") {
            Box(
                modifier = Modifier
                    .absoluteOffset(x = offset_inside_rot_box)
                    .width(eventWidth)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }

    }


    val largeGaps = findLargeGaps(flight_events, 18)

    largeGaps.forEach { gap ->
        //calculate offset of beginning of rotationbox in .dp

        val offset_inside_rot_box: Dp = ( convert_ms_to_dp(gap.second.startTime!!.time - starttime.time, cellWidth) +
                convert_ms_to_dp(gap.first.startTime!!.time - starttime.time, cellWidth) ) * 0.5f


        val displayed_hotel = determine_hotel(gap.first, gap.second)

        Text(text = displayed_hotel.toString(),
            modifier = Modifier
                .absoluteOffset(x = offset_inside_rot_box, y = (-1).dp),
            textAlign = TextAlign.Center,

            fontSize = 8.sp)


    }

}

fun determine_hotel(first: DutyEvent, second: DutyEvent): String? {

    if (first.startTime!!.time < second.startTime!!.time){
        return first.endLocation
    } else {
        return second.endLocation

    }

}

fun findLargeGaps(events: List<DutyEvent>, thresholdHours: Long): List<Pair<DutyEvent, DutyEvent>> {
    val largeGaps = mutableListOf<Pair<DutyEvent, DutyEvent>>()
    val thresholdMillis = TimeUnit.HOURS.toMillis(thresholdHours)

    for (i in 0 until events.size - 1) {
        val currentEvent = events[i]
        val nextEvent = events[i + 1]

        // Ensure both current and next events have valid startTime and endTime
        if (currentEvent.endTime != null && nextEvent.startTime != null) {
            val gapMillis = nextEvent.startTime.time - currentEvent.endTime.time
            if (gapMillis > thresholdMillis) {
                largeGaps.add(Pair(currentEvent, nextEvent))
            }
        }
    }
    return largeGaps
}

fun convert_ms_to_dp(ms: Long, cellWidth: Dp): Dp {
    // Convert milliseconds to fraction of one DayCell

    return cellWidth * ( ms / 1000f / 60 / 60 / 24 )

}



//determine starttime of first event
fun earliestEventWithValidTime(events: List<DutyEvent>): DutyEvent? {
    return events.filter { it.startTime != null }  // Filter out events with no valid time
        .minByOrNull { it.startTime!! }   // Find the event with the minimum time
}

//determine endtime of last event
fun latestEventWithValidTime(events: List<DutyEvent>): DutyEvent? {
    return events.filter { it.endTime != null }  // Filter out events with no valid time
        .maxByOrNull { it.endTime!! }   // Find the event with the maximum time
}

@Preview (showBackground = true, backgroundColor = 0xFFFFFF, widthDp = 50)
@Composable
fun MultiEventRectanglePreview() {

    EventRectangle( events = TestEvents.exampleRotation, cellWidth = 48.dp)

}

@Preview (showBackground = true, backgroundColor = 0xFFFFFF, widthDp = 50)
@Composable
fun SingleEventRectanglePreview() {

    EventRectangle( events = listOf(TestEvents.exampleFlight), cellWidth = 48.dp)

}