package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.domain.utils.DateConverter
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun verticalCalender(
    events: List<DutyEvent>,
    padding: PaddingValues
) {

    // Sort events by date
    val sortedEvents = events.sortedBy { it.day }

    // Get the earliest and latest event dates
    val earliestDate = sortedEvents.firstOrNull()?.day ?: return
    val latestDate = sortedEvents.lastOrNull()?.day ?: return

    // Generate a list of all dates between the earliest and latest event dates
    val allDates = mutableListOf<Date>()
    val calendar = Calendar.getInstance()
    calendar.time = earliestDate
    while (calendar.time <= latestDate) {
        allDates.add(calendar.time.clone() as Date)
        calendar.add(Calendar.DATE, 1)
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
            LazyColumn {
                items(allDates.size) { index ->
                    val currentDate = allDates[index]

                    // Display separator for each day
                    Dayseparator(day = currentDate)

                    // Display events for the day
                    sortedEvents.filter { event ->
                        val eventDate = Calendar.getInstance()
                        eventDate.time = event.day
                        eventDate.get(Calendar.DAY_OF_MONTH) == currentDate.date
                    }.forEach { event ->
                        DutyEventItem(dutyevent = event)
                    }
                }
            }
        }
    }
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

    verticalCalender(events = events, padding = PaddingValues(0.dp))


}