package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.domain.utils.DateConverter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun verticalCalender(
    listState: LazyListState,
    events: List<DutyEvent>,
    padding: PaddingValues
) {

    // get rid of HOTEL Events, if there is a Flight on the same day
    val filteredEvents = events
        .groupBy { it.day }
        .flatMap { (day, eventsOnDay) ->
            if (eventsOnDay.any { it.eventType == "FLIGHT" && it.eventDetails != "X" }) {
                eventsOnDay.filter { it.eventType != "HOTEL" }
            } else {
                eventsOnDay
            }
        }


    // Sort events by date
    val sortedEvents = filteredEvents.sortedBy { it.day }

    // Get the earliest and latest event dates
    val earliestDate = sortedEvents.firstOrNull()?.day ?: return
    val latestDate = getEndOfMonth(sortedEvents.lastOrNull()?.day!!) ?: return

    // Generate a list of all dates between the earliest and latest event dates
    val allDates = mutableListOf<Date>()
    val calendar = Calendar.getInstance()
    calendar.time = earliestDate
    while (calendar.time <= latestDate) {
        allDates.add(calendar.time.clone() as Date)
        calendar.add(Calendar.DATE, 1)
    }

    // Find the index of the current day
    val today = Calendar.getInstance().time
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDayIndex = allDates.indexOfFirst { dateFormatter.format(it) == dateFormatter.format(today) }


    LaunchedEffect(Unit) {

        if (currentDayIndex in 0 until allDates.size) {
            listState.scrollToItem(currentDayIndex)
        }

    }


    Surface(androidx.compose.ui.Modifier.padding(padding)) {
            Column(
                modifier = Modifier.padding(0.dp)
            ) {
                Text(
                    text = "Your Next Flights",
                    modifier = Modifier.padding(10.dp)
                )
                Divider()
                LazyColumn (
                    state = listState
                ){
                    items(allDates.size) { index ->
                        val currentDate = allDates[index]

                        // Display separator for each day
                        Dayseparator(day = currentDate)

                        // Display events for the day
                        sortedEvents.filter { event ->
                            val eventDate = Calendar.getInstance()
                            eventDate.time = event.day
                            areDatesOnSameDay(event.day,currentDate)
                        }.forEach { event ->
                            DutyEventItem(dutyevent = event)
                        }
                    }
                }
            }
        }


}

fun getEndOfMonth(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date

    // Add one month
    calendar.add(Calendar.MONTH, 0)

    // Set the day to the last day of the month
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))

    return calendar.time
}

fun areDatesOnSameDay(date1: Date, date2: Date): Boolean {
    val calendar1 = Calendar.getInstance()
    val calendar2 = Calendar.getInstance()

    calendar1.time = date1
    calendar2.time = date2

    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
            calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
}


@Preview
@Composable
fun showCalender() {
    val dep_time = DateConverter.convertToTimestamp("2024-04-19T12:00:00Z")
    val arr_time = DateConverter.convertToTimestamp("2024-04-19T18:00:00Z")
    val day = DateConverter.convertfromDateStamp("2024-04-19Z")

    val exampleFlight: DutyEvent = DutyEvent(
        day = day,
        _links = null,
        endLocation = "JFK",
        startLocation = "FRA",
        wholeDay = false,
        eventAttributes = null,
        eventDetails = "LH585",
        startTime = dep_time,
        endTime = arr_time,
        endTimeZoneOffset = -330,
        eventCategory = "FLIGHT",
        eventType = "FLIGHT",
        startTimeZoneOffset = 0,
    )


    val events = listOf(exampleFlight)

    verticalCalender(
        listState = rememberLazyListState(),
        events = events, padding = PaddingValues(0.dp))


}