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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
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

            val eventWidth = if (blocked_by_sunday) cutoffeventWidth else raweventWidth
            val exceedingWidth = max(0.dp, eventWidth - cellWidth)

            Log.i(TAG, "MultiEventRectangle: ${earliestEvent.startLocation} - ${earliestEvent.endLocation}")
            Log.i(TAG, "MultiEventRectangle: $blocked_by_sunday")
            Log.i(TAG, "MultiEventRectangle: $eventWidth")

            Box(
                modifier = Modifier
                    .requiredWidth(eventWidth)
                    .absoluteOffset(x = startPosition + (exceedingWidth * 0.5f))
                    .height(20.dp)
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
                    .absoluteOffset(x =(exceedingWidth * 0.5f))
                    .height(20.dp)
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

        //calculate time to sunday evening
        val sunday_evening = Calendar.getInstance().apply {time = earliestEvent.startTime}
        sunday_evening.set(Calendar.DAY_OF_WEEK, 7)
        sunday_evening.add(Calendar.DATE,1)
        sunday_evening.set(Calendar.HOUR_OF_DAY, 23)
        sunday_evening.set(Calendar.MINUTE, 59)
        sunday_evening.set(Calendar.SECOND, 59)
        val time_to_sunday_evening = sunday_evening.time.time - earliestEvent.startTime.time

        Log.i("SundayEVENINGCALC", "time_to_sunday_evening: $time_to_sunday_evening")

        return Pair(true, cellWidth * ( time_to_sunday_evening / 1000f / 60 / 60 / 24 ).toFloat())




    }


}

@Composable
fun FillRotationBox(skipped_width: Dp, starttime: Date, endtime: Date, events: List<DutyEvent>, cellWidth: Dp) {

    val TAG = "FillRotationBox"

    val flight_events = events.filter { it.eventCategory == "flight" && it.eventDetails != "X" }.sortedBy { it.startTime }


    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.absoluteOffset(x = - skipped_width)
    ) {
        flight_events.forEach { flight_event ->
            val (_ ,eventWidth) = calculateEventPositionAndWidth(
                startevent = flight_event,
                endevent = flight_event,
                cellWidth = cellWidth
            )

            Box(
                modifier = Modifier
                    .absoluteOffset(x = skipped_width)
                    .requiredWidth(eventWidth)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )

            //empty space to next flight event

            if (flight_events.indexOf(flight_event) < (flight_events.size-1) ) {

                val time_to_next_flight = flight_events[flight_events.indexOf(flight_event) + 1].startTime!!.time - flight_event.endTime!!.time
                val spacer_to_next_flight =  cellWidth * ( time_to_next_flight / 1000 / 60 / 60 / 24 ).toFloat()

                Box(
                    modifier = Modifier
                        .absoluteOffset(x = skipped_width)
                        .requiredWidth(spacer_to_next_flight )
                        .height(20.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {

                    if (spacer_to_next_flight > 30.dp){
                        Text(textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            text = flight_event.endLocation.toString())
                    }

                }
            }

        }
    }


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