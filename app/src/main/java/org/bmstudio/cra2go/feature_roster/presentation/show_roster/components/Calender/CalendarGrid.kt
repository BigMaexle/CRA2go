package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.bmstudio.cra2go.feature_roster.domain.model.DisplayEvent
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.TestEvents.TestEvents
import org.bmstudio.cra2go.ui.theme.CRA2goTheme
import java.util.Calendar

@Composable
fun CalendarGrid(events: List<DutyEvent>, currentMonth: Calendar,
                 selectedDateEvents: MutableState<List<DutyEvent>>,
                 selectedDay: MutableState<Calendar>) {



    val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfMonth = currentMonth.clone() as Calendar
    firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
    val startingDayOfWeek = determineDay(firstDayOfMonth)
    val days = (1..daysInMonth).toList()
    val paddingDaysBefore = (-startingDayOfWeek + 1 .. 0).toList()
    val paddingDaysAfter = List((7 - (startingDayOfWeek + daysInMonth) % 7) % 7) { -1 }
    val allDays = paddingDaysBefore + days + paddingDaysAfter

    val eventsToBeDisplayed = createDisplayEventList(events,currentMonth)


    LazyVerticalGrid(
        columns = GridCells.Fixed(7)
    ) {
        items(allDays.size) { index ->

            val day = allDays[index]

            val eventsForDay = events.filter { event ->
                val eventcal = Calendar.getInstance().apply { time = event.day }
                val dayofmonth = Calendar.getInstance().apply { time = firstDayOfMonth.time }
                    .apply { add(Calendar.DAY_OF_MONTH, day) }

                //compare if the event day is the same as the day of the month
                eventcal.get(Calendar.YEAR) == dayofmonth.get(Calendar.YEAR) &&
                        eventcal.get(Calendar.MONTH) == dayofmonth.get(Calendar.MONTH) &&
                        eventcal.get(Calendar.DAY_OF_MONTH) == dayofmonth.get(Calendar.DAY_OF_MONTH)
            }



            DayCell(
                currentMonth = currentMonth,
                day = day,
                eventsForDay = eventsForDay,
                selectedEvents = selectedDateEvents,
                selectedDay = selectedDay,
                activeDutyEvents = eventsToBeDisplayed[index]
            )

        }
    }
}

fun createDisplayEventList(events: List<DutyEvent>, currentMonth: Calendar): List<List<List<DisplayEvent>>> {


    //THIS IS WHERE THE MAGIC HAPPENS.
    //Input: Random List of DutyEvents and the current Month

    // Output: A List of events to be displayed.
    // Step 1. Sort all DutyEvents to Rotations, to be displayed as one -> rotations: List<DisplayEvents>
    // All Rotations that begin at Day index are in List so e[index] = List<rotation>
    // All in a List e

    // Step one,
    // Take a List: DUTYEVENTS create a List:DISPLAYEVENTS rotations = same rotationID, if you have no rotID, you are single Event

    val groupedEvents = groupEventsByRotationID(events)

}

fun isOnCurrentDay(event: DutyEvent, dayofmonth: Calendar): Boolean {

    if (event.startTime != null){
        val eventcal = Calendar.getInstance().apply { time = event.startTime }
        if (eventcal.get(Calendar.YEAR) == dayofmonth.get(Calendar.YEAR) &&
            eventcal.get(Calendar.MONTH) == dayofmonth.get(Calendar.MONTH) &&
            eventcal.get(Calendar.DAY_OF_MONTH) == dayofmonth.get(Calendar.DAY_OF_MONTH)
        ){ return true}
    }
    if (event.endTime != null){
        val eventcal = Calendar.getInstance().apply { time = event.endTime }
        if (eventcal.get(Calendar.YEAR) == dayofmonth.get(Calendar.YEAR) &&
            eventcal.get(Calendar.MONTH) == dayofmonth.get(Calendar.MONTH) &&
            eventcal.get(Calendar.DAY_OF_MONTH) == dayofmonth.get(Calendar.DAY_OF_MONTH)
        ){ return true}
    }
    if (event.day != null){
        val eventcal = Calendar.getInstance().apply { time = event.day }
        if (eventcal.get(Calendar.YEAR) == dayofmonth.get(Calendar.YEAR) &&
            eventcal.get(Calendar.MONTH) == dayofmonth.get(Calendar.MONTH) &&
            eventcal.get(Calendar.DAY_OF_MONTH) == dayofmonth.get(Calendar.DAY_OF_MONTH)
        ){ return true}
    }

    return false

}

fun determineDay(firstDayOfMonth: Calendar): Int {
    return if (firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1 == 0) {
        7
    } else{
        firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1
    }
}

@Preview (showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun CalendarGridPreview() {

    val testcal = Calendar.getInstance()
    testcal.set(Calendar.MONTH,Calendar.DECEMBER)

    CRA2goTheme {
        CalendarGrid(events = TestEvents.exampleRotation, currentMonth = testcal,
            selectedDateEvents = remember { mutableStateOf(listOf()) },
            selectedDay = remember { mutableStateOf(Calendar.getInstance()) })
        }
}
