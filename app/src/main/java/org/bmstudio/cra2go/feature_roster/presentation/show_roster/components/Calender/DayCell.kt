package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.bmstudio.cra2go.feature_roster.domain.model.DisplayEvent
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.TestEvents.TestEvents
import org.bmstudio.cra2go.ui.theme.CRA2goTheme
import java.util.Calendar
import java.util.Date


@Composable
fun DayCell(
    currentMonth: Calendar,
    day: Int,
    eventsForDay: List<DutyEvent>,
    selectedEvents: MutableState<List<DutyEvent>>,
    selectedDay:MutableState<Calendar>,
    activeDutyEvents: List<List<DisplayEvent>>) {
    //initial height set at 0.dp
    var cellWidth by remember { mutableStateOf(0.dp) }

    val density = LocalDensity.current




    val maximumdays = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val minimumdays = currentMonth.getActualMinimum(Calendar.DAY_OF_MONTH)

    if (day < minimumdays-1 || day > maximumdays-1) {
        //DayCell is not in active Month (PaddingDays)
        return
    }

    //establish Calender, current Day, Time: Midnight
    val daycalender = Calendar.getInstance().apply { time = currentMonth.time }
    daycalender.set(Calendar.DAY_OF_MONTH, day+1)
    daycalender.set(Calendar.HOUR_OF_DAY, 0)
    daycalender.set(Calendar.MINUTE, 0)

    Box(
        modifier = Modifier
            .height(48.dp)
            .clickable {
                selectedEvents.value = eventsForDay
                selectedDay.value = daycalender
            }
            .border(0.1.dp, Color.LightGray)
            .onGloballyPositioned {
                cellWidth = with(density) {
                    it.size.width.toDp()
                }
            }
            .zIndex(0f)
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val circle_background = if (isSameDay(
                    day = daycalender,
                    selectedDay = Calendar.getInstance()))
                MaterialTheme.colorScheme.secondaryContainer else Color.Transparent

            Box(modifier = Modifier.padding(2.dp)){
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(22.dp)
                            .background(circle_background),

                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(
                            text = (day + 1).toString(),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
            }


            Spacer(modifier = Modifier.weight(1f))

            for (events in activeDutyEvents) {

                //Display Event Rectangle
                    EventRectangle(
                        events = events,
                        cellWidth = cellWidth
                    )

            }

            Spacer(modifier = Modifier.height(5.dp))


        }
    }
}

@Composable
fun isSameDay(day: Calendar, selectedDay: Calendar): Boolean {


    if(day.get(Calendar.DAY_OF_YEAR) == selectedDay.get(Calendar.DAY_OF_YEAR)
        && day.get(Calendar.YEAR) == selectedDay.get(Calendar.YEAR)){
        return true
    }

    return false

}

fun strip_rotation_to_start_Monday_Midnight(rotation: List<DutyEvent>,day: Calendar): List<DutyEvent> {
    // Ensure the "day" Calendar object is set to midnight
    val midnight = day.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

        // Filter and adjust events
    val adjustedEvents = rotation.mapNotNull { event ->
            when {
                event.startTime == null -> null // Event has no start time
                event.endTime == null -> null // Event has no end time
                event.startTime >= midnight -> event // Event starts at or after midnight
                event.endTime > midnight -> event.copy(startTime = midnight)  // Event spans across midnight
                else -> null // Event ends before midnight
            }
        }

    if (rotation.size == 1 && rotation.first().wholeDay == true){
        Log.i("rotation_starts_at_day", rotation.first().toString())
        return listOf(rotation.first())
    }

    // Add a dummy DutyEvent to indicate cropping if the list was actually cropped
    if (rotation.any { it.startTime != null && it.startTime < midnight }) {
        val dummyEvent = DutyEvent(
            startTime = midnight,
            endTime = Date(midnight.time + 1000), // Midnight + 1 second
            eventCategory = "dummy",
            eventType = "dummy",
            eventDetails = "dummy",
            startLocation = "",
            endLocation = "",
            day = Date(midnight.time),
            endTimeZoneOffset = null,
            startTimeZoneOffset = null,
            wholeDay = false,
            eventAttributes = null,
            _links = null
        )
        return listOf(dummyEvent) + adjustedEvents
    }
    return adjustedEvents
}

fun rotation_starts_at_day(rotation: List<DutyEvent>, daycalender: Calendar): Boolean {

    if (rotation.size == 1 && rotation.first().wholeDay == true){
        Log.i("rotation_starts_at_day", rotation.first().toString())
        return true
    }

    val rotation_filtered_by_invalid_startTime = rotation.filter { it.startTime != null }
    val firstEvent = rotation_filtered_by_invalid_startTime.sortedBy { it.startTime }.firstOrNull()
    if (firstEvent == null) { return false }
    if (firstEvent.startTime == null) return false
    val day_of_first_event = Calendar.getInstance().apply { time = firstEvent.startTime }
    return day_of_first_event.get(Calendar.DAY_OF_YEAR) == daycalender.get(Calendar.DAY_OF_YEAR)
}

fun isMonday(daycalender: Calendar): Boolean {
    return daycalender.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
}

@Preview (showBackground = true, backgroundColor = 0xFFFFFF, widthDp = 48)
@Composable
fun OneFlightDayCellPreview(){

    CRA2goTheme {

        DayCell(
            currentMonth = Calendar.getInstance(),
            day = 0,
            eventsForDay = listOf(),
            selectedEvents = remember { mutableStateOf(listOf()) },
            selectedDay = remember { mutableStateOf(Calendar.getInstance()) },
            activeDutyEvents = listOf(listOf()),
        )

    }

}

@Preview (showBackground = true, backgroundColor = 0xFFFFFF, widthDp = 48)
@Composable
fun RotationFlightDayCellPreview(){

    CRA2goTheme {

        DayCell(
            currentMonth = Calendar.getInstance(),
            day = 0,
            eventsForDay = listOf(),
            selectedEvents = remember { mutableStateOf(listOf()) },
            selectedDay = remember { mutableStateOf(Calendar.getInstance()) },
            activeDutyEvents = listOf(listOf()),
        )

    }

}
