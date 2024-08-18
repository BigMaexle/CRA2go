package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components

import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.launch
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.domain.utils.DateConverter
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarView(
    events: List<DutyEvent>,
    padding: PaddingValues
)  {
        // MutableState for current month
        val currentMonth = remember { mutableStateOf(Calendar.getInstance()) }

        // MutableState for selected date events
        val selectedDateEvents = remember { mutableStateOf<List<DutyEvent>>(listOf()) }

        val MAX_MONTHS = 100

        val selectedMonth = rememberPagerState (pageCount = {MAX_MONTHS}, initialPage = MAX_MONTHS/2)

        // UI Layout
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = selectedMonth,
            verticalAlignment = Alignment.Top
        ) { page ->
            val month = currentMonth.value.clone() as Calendar
            month.add(Calendar.MONTH, page - MAX_MONTHS / 2)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Display month and year
                Text(
                    text = "${month.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())} ${month.get(Calendar.YEAR)}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Calendar controls
                CalendarControls(currentMonth, selectedMonth)

                // Calendar grid with events
                val daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)
                val days = (1..daysInMonth).toList()
                CalendarGrid(events, month, selectedDateEvents)

                // Display events for selected date at the bottom
                EventList(selectedDateEvents.value)
            }
        }
    }


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarControls(currentMonth: MutableState<Calendar>, selectedMonth: PagerState) {
    val scope = rememberCoroutineScope()
    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = {
            scope.launch {
                //currentMonth.value.add(Calendar.MONTH, -1)
                selectedMonth.scrollToPage(selectedMonth.currentPage - 1)
            }

        }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
        }
        IconButton(onClick = {
            scope.launch {
                //currentMonth.value.add(Calendar.MONTH, + 1)
                selectedMonth.scrollToPage(selectedMonth.currentPage + 1)
            }
        }) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
        }
    }
}

@Composable
fun CalendarGrid(events: List<DutyEvent>, currentMonth: Calendar, selectedDateEvents: MutableState<List<DutyEvent>>) {
    val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfMonth = currentMonth.clone() as Calendar
    firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
    val startingDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1 // Adjusting for zero-based indexing
    val days = (1..daysInMonth).toList()
    val paddingDaysBefore = List(startingDayOfWeek) { -1 }
    val paddingDaysAfter = List((7 - (startingDayOfWeek + daysInMonth) % 7) % 7) { -1 }
    val allDays = paddingDaysBefore + days + paddingDaysAfter
    LazyVerticalGrid(
        columns = GridCells.Fixed(7)) {
        items(allDays.size) { index ->

            DayCell(currentMonth = currentMonth, day = index, events = events)
            
        }
    }
}

@Composable
fun calculateEventPositionAndWidth(startTime: Date, endTime: Date, cellWidth: Dp): Pair<Dp, Dp> {

      // Use LocalDensity to convert Dp to pixels
    val density = LocalDensity.current.density
    val cellWidthPx = with(LocalDensity.current) { cellWidth.toPx() } // Convert Dp to pixels

    // Calculate the start position as a fraction of the 24-hour day
    val startHourFraction = (startTime.hours + startTime.minutes / 60f)

    // Calculate the end position as a fraction of the 24-hour day
    val endHourFraction = (endTime.hours + endTime.minutes / 60f)

    // Convert these fractions into pixels for position and width
    val startPositionPx = (startHourFraction / 24f) * cellWidthPx
    val eventWidthPx = ((endHourFraction - startHourFraction) / 24f) * cellWidthPx

    // Convert back to Dp for usage in Composable
    val startPosition = with(LocalDensity.current) { startPositionPx.toDp() }
    val eventWidth = with(LocalDensity.current) { eventWidthPx.toDp() }

    return Pair(startPosition, eventWidth)
}

fun determine_event_icon_for_day(eventsForDay: List<DutyEvent>): ImageVector? {
    eventsForDay.forEach { event ->
        if (event.eventType == "FLIGHT" && event.eventDetails != "X") return Icons.Filled.AirplanemodeActive}
    eventsForDay.forEach { event ->
        if (event.eventType == "HOTEL") return Icons.Filled.Hotel }
    return null

}

@Composable
fun EventList(events: List<DutyEvent>) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider()
        events.forEach { event ->
            DutyEventItem(dutyevent = event)
        }
        Divider()
    }
}

@Composable
fun DayCell(currentMonth: Calendar,day: Int, events: List<DutyEvent>) {
    val cellWidth = 48.dp // Set the width of each cell
    val density = LocalDensity.current.density

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(cellWidth, 48.dp)
            .clickable {
                //EventList(events = events)
            }
            //.background(Color.LightGray)
            //.border(1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the day number
            Text(
                text = day.toString(),
                style = MaterialTheme.typography.bodySmall
            )

            // Display each event that occurs on this day
            val eventsForDay = events.filter { event ->
                val eventCalendar = Calendar.getInstance()
                eventCalendar.time = event.day
                eventCalendar.get(Calendar.DAY_OF_MONTH) == day && eventCalendar.get(Calendar.MONTH) == currentMonth.get(
                    Calendar.MONTH
                )
            }.forEach{ event ->
                if (event.startTime == null) return
                if (event.endTime == null) return
                val (startPosition, eventWidth) = calculateEventPositionAndWidth(event.startTime, event.endTime, cellWidth)

                Box(
                    modifier = Modifier
                        .offset(x = startPosition)
                        .width(eventWidth)
                        .height(10.dp)
                        .background(Color.Blue)
                )
            }
        }
    }
}
fun Date.toCalendarDay(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}

val Date.hours: Int
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

val Date.minutes: Int
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.MINUTE)
    }


@Preview (showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun CalendarViewPreview() {
    val dep_time = DateConverter.convertToTimestamp("2024-04-19T23:00:00Z")
    val arr_time = DateConverter.convertToTimestamp("2024-04-20T10:00:00Z")
    val day = DateConverter.convertfromDateStamp("2024-04-19Z")

    val exampleFlight: DutyEvent = DutyEvent(
        day = day,
        _links = null,
        endLocation = "PVG",
        startLocation = "FRA",
        wholeDay = false,
        eventAttributes = null,
        eventDetails = "LH8000",
        startTime = dep_time,
        endTime = arr_time,
        endTimeZoneOffset = -360,
        eventCategory = "FLIGHT",
        eventType = "FLIGHT",
        startTimeZoneOffset = -120,
    )


    CalendarView(
        events = listOf(),
        padding = PaddingValues(0.dp)
    )
}

@Preview (showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun EventBoxPreview(){
    val dep_time = DateConverter.convertToTimestamp("2024-04-19T23:00:00Z")
    val arr_time = DateConverter.convertToTimestamp("2024-04-20T10:00:00Z")
    val day = DateConverter.convertfromDateStamp("2024-04-19Z")

    val exampleFlight: DutyEvent = DutyEvent(
        day = day,
        _links = null,
        endLocation = "PVG",
        startLocation = "FRA",
        wholeDay = false,
        eventAttributes = null,
        eventDetails = "LH8000",
        startTime = dep_time,
        endTime = arr_time,
        endTimeZoneOffset = -360,
        eventCategory = "FLIGHT",
        eventType = "FLIGHT",
        startTimeZoneOffset = -120,
    )
    //EventBox(event = exampleFlight)


}

