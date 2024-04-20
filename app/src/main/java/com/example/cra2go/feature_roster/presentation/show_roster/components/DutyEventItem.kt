package com.example.cra2go.feature_roster.presentation.show_roster.components

import android.icu.util.GregorianCalendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cra2go.classes.FlightEvent
import com.example.cra2go.feature_roster.domain.model.DutyEvent
import com.example.cra2go.feature_roster.domain.utils.DateConverter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun DutyEventItem(dutyevent: DutyEvent) {

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        Column (
            Modifier.padding(8.dp)
        ){
            if (dutyevent.eventCategory == "FLIGHT"){
            ShowFlightAirportRow(
                DepAirport = dutyevent.startLocation,
                ArrAirport = dutyevent.endLocation
            )}
            if (dutyevent.eventCategory == "GROUNDEVENT") {
                dutyevent.eventDetails?.let { Text (
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(20.dp),
                    text = it,
                    textAlign = TextAlign.Center), }
            }
            if (dutyevent.wholeDay == false) {
                ShowDutyTimes(startTime = dutyevent.startTime, endTime = dutyevent.endTime)
            }
        }


    }
}

@Composable
fun ShowFlightAirportRow(DepAirport:String?,ArrAirport:String?){

    Row (
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column (
            modifier = Modifier
        ){
            Text(text = "Departure", fontSize = 8.sp,textAlign = TextAlign.Left)
            if (DepAirport != null) {
                Text(text = DepAirport, textAlign = TextAlign.Left)
            }
        }
        Spacer(Modifier.weight(1f))
        Column (modifier = Modifier){
            Text(text = "Arrival", fontSize = 8.sp,textAlign = TextAlign.End)
            if (ArrAirport != null) {
                Text(text = ArrAirport,textAlign = TextAlign.End)
            }
        }
    }

}



@Composable
fun ShowDutyTimes(startTime: Date?, endTime: Date?){
    val formatter = SimpleDateFormat("HH:mm 'UTC'")
    CompositionLocalProvider (
        LocalTextStyle provides TextStyle(fontSize = 12.sp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val off = startTime?.let { formatter.format(it) }
            Text(text = "$off")
            Spacer(modifier = Modifier.weight(1f))
            val on = endTime?.let { formatter.format(it) }
            Text(text = "$on")
        }
    }
}

@Preview
@Composable
fun showExampleFlight(){
    val dep_time = DateConverter.convertToTimestamp("2024-04-19T12:00:00Z")
    val arr_time = DateConverter.convertToTimestamp("2024-04-19T18:00:00Z")

    val exampleFlight: DutyEvent = DutyEvent(
    _links = null,
    endLocation = "JFK",
    startLocation = "FRA",
    wholeDay = false,
    eventAttributes = null,
    eventDetails = "LH585",
    startTime = dep_time,
    endTime = arr_time,
    endTimeZoneOffset = -330,
    eventCategory = "FLIGHT",
    eventType = "FLIGHT",
    startTimeZoneOffset = 0,
    )
    DutyEventItem(dutyevent = exampleFlight)
    }

@Preview
@Composable
fun showRES(){
    val dep_time = DateConverter.convertToTimestamp("2024-04-19T12:00:01Z")
    val arr_time = DateConverter.convertToTimestamp("2024-04-19T18:00:00Z")


    val exampleRES = DutyEvent(
        _links = null,
        endLocation = "JFK",
        startLocation = "FRA",
        wholeDay = true,
        eventAttributes = null,
        eventDetails = "RES",
        startTime = nul,
        endTime = null,
        endTimeZoneOffset = -330,
        eventCategory = "GROUNDEVENT",
        eventType = "FLIGHT",
        startTimeZoneOffset = 0,
    )
    DutyEventItem(dutyevent = exampleRES)
}