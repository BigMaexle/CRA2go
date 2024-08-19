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
fun RotationRectangle(starting: Boolean,rotations: List<DutyEvent>, cellWidth: Dp) {

    val displayevents = emptyList<DutyEvent>().toMutableList()

    if (rotations.isEmpty()) return
    if (rotations.size == 1) {
        SingleEventRectangle(event = rotations.first(), cellWidth = cellWidth)
    }
    if (rotations.size > 1) {

        MultiEventRectangle(starting = starting,events = rotations, cellWidth = cellWidth)

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

