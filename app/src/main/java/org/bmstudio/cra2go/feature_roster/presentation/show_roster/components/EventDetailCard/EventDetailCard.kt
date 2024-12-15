package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.EventDetailCard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.TestEvents.TestEvents
import org.bmstudio.cra2go.ui.theme.CRA2goTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailCard(event: DutyEvent,onClick: () -> Unit = {}){


    Box(
        modifier = Modifier
            .padding(0.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .border(BorderStroke(0.2.dp, Color.Black), RoundedCornerShape(10.dp))
            .width(IntrinsicSize.Max)
            .clickable { onClick() }
    ){

        Column (modifier = Modifier.padding(10.dp)){

            Text(text = "Hello")
        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TestEventCard(){
    CRA2goTheme {
        EventDetailCard(event = TestEvents.exampleFlight)
    }
}