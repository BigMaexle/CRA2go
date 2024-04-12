package com.example.cra2go.classes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cra2go.ui.theme.CRA2goTheme
import java.sql.Time
import java.util.Date

class FlightEvent(
    val date: Date,
    val offblock: Time,
    val onblock: Time,
    val depatureAirport: String,
    val destinationAirport: String,
) {
}

@Composable
fun DisplayFlight(flight: FlightEvent) {

    Card (
        modifier = Modifier.width(200.dp)
    ){
        Column (
            Modifier.padding(8.dp)
        ){
            ShowFlightAirportRow(
                DepAirport = flight.depatureAirport,
                ArrAirport = flight.destinationAirport
            )
            ShowFlightTimes(offBlock = flight.offblock, onBlock = flight.onblock)
        }


    }
}

@Composable
fun ShowFlightAirportRow(DepAirport:String,ArrAirport:String){
    Row (
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column (
            modifier = Modifier
        ){
            Text(text = "Departure", fontSize = 8.sp,textAlign = TextAlign.Left)
            Text(text = DepAirport, textAlign = TextAlign.Left)
        }
        Spacer(Modifier.weight(1f))
        Column (modifier = Modifier){
            Text(text = "Arrival", fontSize = 8.sp,textAlign = TextAlign.End)
            Text(text = ArrAirport,textAlign = TextAlign.End)
        }
    }

}

@Composable
fun ShowFlightTimes(offBlock: Time,onBlock: Time){
    CompositionLocalProvider (
        LocalTextStyle provides TextStyle(fontSize = 5.sp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = offBlock.toString())
            Spacer(modifier = Modifier.weight(1f))
            Text(text = onBlock.toString())
        }
    }
}

@Preview (showBackground = true)
@Composable
fun ShowSampleFlight(){
    val exampleflight = FlightEvent(
        date = Date(2024,4,12),
        offblock = Time(12,0,0),
        onblock = Time(15,0,0),
        depatureAirport = "FRA",
        destinationAirport = "JFK"
    )
    CRA2goTheme {
        DisplayFlight(exampleflight)
    }
}