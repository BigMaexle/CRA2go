package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent

@Composable
fun SingleEventRectangle(event: DutyEvent, cellWidth: Dp) {

    if (event.eventType == "GROUNDEVENT") {
        val (startPosition, eventWidth) = calculateEventPositionAndWidth(
            startevent = event,
            endevent = event,
            cellWidth = cellWidth
        )

        val color: Color = determineEventColor(event)

        Box(
            modifier = Modifier
                .offset(x = startPosition)
                .width(eventWidth)
                .height(20.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color),
            contentAlignment = Alignment.Center

        )
        {
            Text(
                text = event.eventCategory.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White)
        }
    }


}

@Composable
fun determineEventColor(event: DutyEvent): Color {

    if (event.eventType == "GROUNDEVENT") return MaterialTheme.colorScheme.primary
    if (event.eventType == "FLIGHT") return MaterialTheme.colorScheme.secondary

    return MaterialTheme.colorScheme.onSurface

}
