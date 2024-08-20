package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import java.util.Calendar
import java.util.Date

@Composable
fun MultiEventRectangle(starting:Boolean, events: List<DutyEvent>, cellWidth: Dp) {

    val TAG = "MultiEventRectangle"

    val earliestEvent = earliestEventWithValidTime(events)
    val latestEvent = latestEventWithValidTime(events)

    if (earliestEvent != null && latestEvent != null) {

        val (startPosition, raweventWidth) = calculateEventPositionAndWidth(
            startevent = earliestEvent,
            endevent = latestEvent,
            cellWidth = cellWidth
        )


        if (starting){
            val (blocked_by_sunday,cutoffeventWidth) = cutOffWidthBySunday(earliestEvent,latestEvent,cellWidth)

            val eventWidth = if (blocked_by_sunday) min(cutoffeventWidth,raweventWidth) else raweventWidth
            val exceedingWidth = max(0.dp, eventWidth - cellWidth)

            Log.i(TAG, "MultiEventRectangle: ${earliestEvent.startLocation} - ${earliestEvent.endLocation}")
            Log.i(TAG, "MultiEventRectangle: $blocked_by_sunday")
            Log.i(TAG, "MultiEventRectangle: $eventWidth")

            Box(
                modifier = Modifier
                    .requiredWidth(eventWidth)
                    .absoluteOffset(x = startPosition + (exceedingWidth * 0.5f))
                    .height(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .border(BorderStroke(0.2.dp, Color.Black), RoundedCornerShape(2.dp)),
                contentAlignment = Alignment.CenterStart

            )
            {

                FillRotationBox (
                    skipped_width = 0.dp,
                    starttime = earliestEvent.startTime!!,
                    endtime = latestEvent.endTime!!,
                    events = events,
                    cellWidth = cellWidth)

            }
        } else {


            val eventWidth: Dp = calculateEventWidthFromMonday(latestEvent,cellWidth)
            val exceedingWidth = max(0.dp, eventWidth - cellWidth)

            Box(
                modifier = Modifier
                    .requiredWidth(eventWidth)
                    .absoluteOffset(x = (exceedingWidth * 0.5f))
                    .height(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .border(BorderStroke(0.2.dp, Color.Black), RoundedCornerShape(2.dp)),
                contentAlignment = Alignment.CenterStart

            )
            {



                FillRotationBox (
                    skipped_width = (raweventWidth - eventWidth),
                    starttime = earliestEvent.startTime!!,
                    endtime = latestEvent.endTime!!,
                    events = events,
                    cellWidth = cellWidth)

            }

        }




    }



}

fun calculateEventWidthFromMonday(latestEvent: DutyEvent, cellWidth: Dp): Dp {

    if (latestEvent.startTime == null) return 0.dp
    if (latestEvent.endTime == null) return 0.dp

    val cal_latestEvent = Calendar.getInstance().apply { time = latestEvent.endTime }
    cal_latestEvent.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    cal_latestEvent.set(Calendar.HOUR_OF_DAY, 0)
    cal_latestEvent.set(Calendar.MINUTE, 0)
    cal_latestEvent.set(Calendar.SECOND, 0)

    val time_from_Monday = latestEvent.endTime.time - cal_latestEvent.time.time

    return cellWidth * ( time_from_Monday / 1000f / 60 / 60 / 24 )


}

fun cutOffWidthBySunday(earliestEvent: DutyEvent, latestEvent: DutyEvent, cellWidth: Dp): Pair<Boolean, Dp> {
    if (earliestEvent.startTime == null) return Pair(false, 0.dp)
    if (latestEvent.endTime == null) return Pair(false, 0.dp)
    val cal_earliestEvent = Calendar.getInstance().apply { time = earliestEvent.startTime }
    val cal_latestEvent = Calendar.getInstance().apply { time = latestEvent.endTime }

    cal_earliestEvent.add(Calendar.DAY_OF_YEAR, -1)
    cal_latestEvent.add(Calendar.DAY_OF_YEAR, -1)

    if (cal_earliestEvent.get(Calendar.WEEK_OF_YEAR) == cal_latestEvent.get(Calendar.WEEK_OF_YEAR)) {
        return Pair(false, 0.dp)
    } else {

        //calculate time to nearest sunday evening
        val sunday_evening = Calendar.getInstance().apply {time = earliestEvent.startTime}
        sunday_evening.set(Calendar.DAY_OF_WEEK, 1)
        sunday_evening.set(Calendar.HOUR_OF_DAY, 23)
        sunday_evening.set(Calendar.MINUTE, 59)
        sunday_evening.set(Calendar.SECOND, 59)

        //make sure it is the NEXT sunday evening
        if (sunday_evening.time.time < earliestEvent.startTime.time) {
            sunday_evening.add(Calendar.WEEK_OF_YEAR, 1)
        }

        val time_to_sunday_evening = sunday_evening.time.time - earliestEvent.startTime.time
        val dp_to_sunday_evening = convert_ms_to_dp(time_to_sunday_evening, cellWidth)

        Log.i("SundayEVENINGCALC", "time_to_sunday_evening: $time_to_sunday_evening")
        Log.i("SundayEVENINGCALC", "dp_to_sunday_evening: $dp_to_sunday_evening")

        return Pair(true,dp_to_sunday_evening)




    }


}

@Composable
fun FillRotationBox(skipped_width: Dp, starttime: Date, endtime: Date, events: List<DutyEvent>, cellWidth: Dp) {

    val TAG = "FillRotationBox"

    val flight_events = events.filter {
        (it.eventCategory == "flight" && it.eventDetails != "X") }.sortedBy { it.startTime }

    flight_events.forEach { event ->

        //calculate offset of beginning of rotationbox in .dp
        val offset_inside_rot_box: Dp = convert_ms_to_dp(event.startTime!!.time - starttime.time, cellWidth)

        val eventWidth: Dp = convert_ms_to_dp(event.endTime!!.time - event.startTime!!.time, cellWidth)

        Box(modifier = Modifier
            .absoluteOffset(x = offset_inside_rot_box - skipped_width )
            .width(eventWidth)
            .height(20.dp)
            .background(MaterialTheme.colorScheme.primary))

    }


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

@Preview (showBackground = true)
@Composable
fun MultiEventRectanglePreview() {

    val alreadydisplayed = 0.dp

    val startoffset = 20.dp

    Box(modifier = Modifier
        .requiredWidth(100.dp)
        .absoluteOffset(x = 0.dp)
        .height(20.dp)
        .clip(RoundedCornerShape(2.dp))
        .border(BorderStroke(0.2.dp, Color.Black), RoundedCornerShape(2.dp))
        .background(Color.White),
        contentAlignment = Alignment.CenterStart)
    {
        Box(modifier = Modifier
            .absoluteOffset(x = 0.dp)
            .width(10.dp)
            .height(20.dp)
            .background(MaterialTheme.colorScheme.primary))
        val spacer_to_next_flight = 80.dp
        Box(
            modifier = Modifier
                .absoluteOffset(x = 10.dp)
                .width(spacer_to_next_flight)
                .height(20.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {

            if (spacer_to_next_flight > 30.dp){
                Text(textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    text = "JFK")
            }

        }
        Box(modifier = Modifier
            .absoluteOffset(x = 90.dp)
            .width(10.dp)
            .height(20.dp)
            .background(MaterialTheme.colorScheme.primary))
    }
}