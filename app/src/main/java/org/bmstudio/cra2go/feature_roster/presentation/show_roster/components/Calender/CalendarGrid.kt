package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import android.util.Log
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import java.util.Calendar

@Composable
fun CalendarGrid(events: List<DutyEvent>, currentMonth: Calendar, selectedDateEvents: MutableState<List<DutyEvent>>) {

    val groupedEvents = groupEventsByRotationID(events)


    val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfMonth = currentMonth.clone() as Calendar
    firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
    val startingDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) -1 // Adjusting for zero-based indexing
    val days = (1..daysInMonth).toList()
    val paddingDaysBefore = (-startingDayOfWeek+1 .. 0).toList()
    val paddingDaysAfter = List((7 - (startingDayOfWeek + daysInMonth) % 7) % 7) { -1 }
    val allDays = paddingDaysBefore + days + paddingDaysAfter
    LazyVerticalGrid(
        columns = GridCells.Fixed(7)
    ) {
        items(allDays.size) { index ->

            val day = allDays[index]


            //convert the index to a day of the month
            val dayofmonth = Calendar.getInstance().apply { time = firstDayOfMonth.time }
                .apply { add(Calendar.DAY_OF_MONTH, day) }

            val rotationsStartAtDay = filterRotationStartByDay(dayofmonth.time,groupedEvents)

            val eventsForDay = events.filter { event ->
                val eventcal = Calendar.getInstance().apply { time = event.day }
                val dayofmonth = Calendar.getInstance().apply { time = firstDayOfMonth.time }
                    .apply { add(Calendar.DAY_OF_MONTH, day) }

                //compare if the event day is the same as the day of the month
                eventcal.get(Calendar.YEAR) == dayofmonth.get(Calendar.YEAR) &&
                        eventcal.get(Calendar.MONTH) == dayofmonth.get(Calendar.MONTH) &&
                        eventcal.get(Calendar.DAY_OF_MONTH) == dayofmonth.get(Calendar.DAY_OF_MONTH)
            }



            // is current in active month
            val isCurrentMonth = currentMonth.get(Calendar.MONTH) == dayofmonth.get(Calendar.MONTH)

            val activeRotations = groupedEvents.filter { eventList ->
                eventList.any { event ->
                    val eventcal = Calendar.getInstance().apply { time = event.day }
                    eventcal.get(Calendar.YEAR) == dayofmonth.get(Calendar.YEAR) &&
                            eventcal.get(Calendar.MONTH) == dayofmonth.get(Calendar.MONTH) &&
                            eventcal.get(Calendar.DAY_OF_MONTH) == dayofmonth.get(Calendar.DAY_OF_MONTH)


                }
            }


            DayCell(
                isCurrentMonth = isCurrentMonth,
                currentMonth = currentMonth,
                day = day,
                events = rotationsStartAtDay,
                eventsForDay = eventsForDay,
                selected = selectedDateEvents,
                activeRotation = activeRotations
            )

        }
    }
}