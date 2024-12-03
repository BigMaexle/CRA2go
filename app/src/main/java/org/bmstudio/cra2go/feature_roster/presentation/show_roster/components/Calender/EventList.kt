package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.DutyEventItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun EventList(events: List<DutyEvent>,day:Calendar) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider()
        val date: Date = day.getTime()
        val format1: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val date1: String = format1.format(date)
        Box(modifier = Modifier.padding(all = 15.dp),
            contentAlignment = Alignment.Center) {
            Text(text = "$date1")
        }
        events.forEach { event ->
            DutyEventItem(dutyevent = event)
        }
        Divider()
    }
}