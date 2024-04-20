package com.example.cra2go.feature_roster.presentation.show_roster

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cra2go.feature_roster.domain.model.DutyEvent
import com.example.cra2go.feature_roster.presentation.show_roster.components.DutyEventItem
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DutyEventScreen (
    viewModel: DutyEventViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    Scaffold (

        topBar = { SmallTopAppBar(title = { Text(text = "CRA2go") },
        ) },

        bottomBar = {BottomAppBarWithTwoButtons(
            onRefreshClick = { viewModel.onEvent(DutyEventsEvent.UpdateRoster) },
            onDeleteClick = {viewModel.onEvent(DutyEventsEvent.DeleteAllEvents)} )}


    ){
            PaddingValues: PaddingValues -> 24.dp

        Surface (Modifier.padding(PaddingValues)){
            Column (
                modifier = Modifier.padding(10.dp)
            ){
                Text(text = "Your Next Flights",
                    modifier = Modifier.padding(10.dp))
                Divider()
                LazyColumn {
                    items(state.events) {item: DutyEvent -> DutyEventItem(dutyevent = item) }
                }
            }
        }

    }
}


@Composable
fun BottomAppBarWithTwoButtons(
    onRefreshClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
            Button(onClick = onDeleteClick, ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onRefreshClick) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }

        }
    }
}

@Preview
@Composable
fun PreviewBottomAppBar() {
    BottomAppBarWithTwoButtons(
        onRefreshClick = {},
        onDeleteClick = {}
    )
}


@Composable
fun ScrollableCalendar() {
    val currentDate = remember { mutableStateOf(Calendar.getInstance()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 5.dp)
    ) {
        Text("Scrollable Calendar", modifier = Modifier.padding(bottom = 8.dp))

        // Horizontal Scrollable Calendar
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            val today = Calendar.getInstance()
            repeat(30) { days ->
                val day = today.clone() as Calendar
                day.add(Calendar.DATE, days)
                item {
                    DayItem(day = day)
                }
            }
        }
    }
}

@Composable
fun DayItem(day: Calendar) {
    val formattedDate = remember(day) {
        val dateFormat = SimpleDateFormat("d. MMM")
        dateFormat.format(day.time)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = formattedDate)
    }
}

@Preview
@Composable
fun PreviewCalendar() {
    ScrollableCalendar()
}