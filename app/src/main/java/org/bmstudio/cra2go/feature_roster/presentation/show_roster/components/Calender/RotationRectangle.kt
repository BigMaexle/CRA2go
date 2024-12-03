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
import org.bmstudio.cra2go.CRA2go
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.TestEvents.TestEvents
import org.bmstudio.cra2go.ui.theme.CRA2goTheme

@Composable
fun RotationRectangle(rotation: List<DutyEvent>, cellWidth: Dp) {

    val rotation_without_Briefing = rotation.filter { it.eventType != "BRIEFING" }

    EventRectangle( events = rotation_without_Briefing, cellWidth = cellWidth)




}

@Preview (showBackground = true,backgroundColor = 0xFFFFFF)
@Composable
fun EventRectanglePreview() {
    CRA2goTheme {
        Box (modifier = Modifier
            .width(200.dp)
            .height(50.dp),
            contentAlignment = Alignment.CenterStart
        )
        {

            RotationRectangle(rotation = TestEvents.exampleRotation, cellWidth = 50.dp)
        }
    }


}

