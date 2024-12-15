package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
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
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.domain.utils.DateConverter
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.TestEvents.TestEvents
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarView(
    events: List<DutyEvent>,
    filter: List<Boolean>,
    padding: PaddingValues
)  {

        // MutableState for current month
        val currentMonth = remember { mutableStateOf(Calendar.getInstance()) }
    
        // MutableState for selected date events
        val selectedDateEvents = remember { mutableStateOf<List<DutyEvent>>(listOf()) }
        val selectedDay = remember {
            mutableStateOf(Calendar.getInstance())
        }

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
                    text = "${month.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)} ${month.get(Calendar.YEAR)}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                // Calendar controls
                CalendarControls(currentMonth, selectedMonth)

                // Weekdays
                Weekdays()


                // Calendar grid with events
                val daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)
                val days = (1..daysInMonth).toList()
                CalendarGrid(events, month, selectedDateEvents,selectedDay,filter)

                // Display events for selected date at the bottom
                EventList(selectedDateEvents.value,selectedDay.value)
            }
        }
    }

@Composable
fun Weekdays() {
    Row (modifier = Modifier){

        for (day in listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")){
            Text(
                text = day,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
        }

    }
    Divider()
}



@Preview (showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun CalendarViewPreview() {

    CalendarView(
        events = TestEvents.exampleRotation,
        padding = PaddingValues(0.dp),
        filter = listOf(true,true,true,true)
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
    //EventRectangle(rotation = exampleFlight, 50.dp)


}

