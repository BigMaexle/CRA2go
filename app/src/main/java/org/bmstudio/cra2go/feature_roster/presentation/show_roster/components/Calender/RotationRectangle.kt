package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent

@Composable
fun RotationRectangle(starting: Boolean,rotation: List<DutyEvent>, cellWidth: Dp) {

    val rotation_without_Briefing = rotation.filter { it.eventType != "BRIEFING" }

    if (rotation.isEmpty()) return
    if (rotation.size == 1) {
        SingleEventRectangle(event = rotation[0], cellWidth = cellWidth)
    }
    if (rotation.size > 1) {
        MultiEventRectangle(starting = starting, events = rotation_without_Briefing, cellWidth = cellWidth)
    }




}

@Preview (showBackground = true)
@Composable
fun EventRectanglePreview() {
    Box (modifier = Modifier
        .width(50.dp)
        .height(50.dp),
        contentAlignment = Alignment.CenterStart
    )
    {

        //RotationRectangle(rotations = TestEvents.exampleFlight, cellWidth = 50.dp)
    }

}

