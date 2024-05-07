package com.example.cra2go.feature_roster.presentation.show_roster.components

import android.icu.util.GregorianCalendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
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
import java.util.Calendar
import java.util.Date


@Composable
fun DutyEventItem(dutyevent: DutyEvent) {

    if (dutyevent.eventType == "HOTEL") {return}
    if (dutyevent.eventType == "BRIEFING") {return}
    if (dutyevent.eventDetails == "X") {return}

    val formatter = SimpleDateFormat("EE dd.MM.yy")

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        Column (
            Modifier.padding(8.dp)
        ){
            if (dutyevent.eventType == "FLIGHT"){
            ShowFlightAirportRow(
                DepAirport = dutyevent.startLocation,
                ArrAirport = dutyevent.endLocation,
                flightno = dutyevent.eventDetails
            )}
            if (dutyevent.eventType == "GROUNDEVENT") {
                dutyevent.eventDetails?.let { Text (
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(20.dp),
                    text = it,
                    textAlign = TextAlign.Center) }
            }
            if (dutyevent.wholeDay == false) {
                ShowDutyTimes(startTime = dutyevent.startTime,
                    endTime = dutyevent.endTime,
                    startoffset = dutyevent.startTimeZoneOffset,
                    endoffset = dutyevent.endTimeZoneOffset)
            }
        }


    }
}

@Composable
fun ShowFlightAirportRow(DepAirport:String?,ArrAirport:String?,flightno:String?){

    Row (
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column (
            modifier = Modifier.weight(1f)
        ){
            Text(text = "Departure", fontSize = 8.sp,textAlign = TextAlign.Left)
            if (DepAirport != null) {
                Text(text = DepAirport, textAlign = TextAlign.Left)
            }
        }

        flightno?.let { Text(text = it,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center) }

        Column (modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End){
            Text(text = "Arrival", fontSize = 8.sp,textAlign = TextAlign.Right)
            if (ArrAirport != null) {
                Text(text = ArrAirport,textAlign = TextAlign.End)
            }
        }
    }

}



@Composable
fun ShowDutyTimes(startTime: Date?, endTime: Date?,startoffset: Int?,endoffset: Int?){
    val formatter = SimpleDateFormat("HH:mm")
    CompositionLocalProvider (
        LocalTextStyle provides TextStyle(fontSize = 12.sp)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val off = startTime?.let { formatter.format(it) }
                Text(text = "$off UTC")
                Spacer(modifier = Modifier.weight(1f))
                val on = endTime?.let { formatter.format(it) }
                Text(text = "$on UTC")
            }
            CompositionLocalProvider(LocalTextStyle provides TextStyle(fontSize = 10.sp)) {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    val c = Calendar.getInstance()
                    c.time = startTime!!
                    if (startoffset != null) {
                        c.add(Calendar.MINUTE,-startoffset)
                    }
                    val offstring = formatter.format(c.time)
                    Text(text = "$offstring LT")
                    Spacer(modifier = Modifier.weight(1f))
                    c.time = endTime!!
                    if (endoffset != null) {
                        c.add(Calendar.MINUTE,-endoffset)
                    }
                    val onstring = formatter.format(c.time)
                    Text(text = "$onstring LT")


                }
                
            }
        }

    }
}

@Preview (showBackground = true)
@Composable
fun showExampleFlight(){
    val dep_time = DateConverter.convertToTimestamp("2024-04-19T12:00:00Z")
    val arr_time = DateConverter.convertToTimestamp("2024-04-19T18:00:00Z")
    val day = DateConverter.convertfromDateStamp("2024-04-19Z")

    val exampleFlight: DutyEvent = DutyEvent(
        day = day,
    _links = null,
    endLocation = "JFK",
    startLocation = "FRA",
    wholeDay = false,
    eventAttributes = null,
    eventDetails = "LH400",
    startTime = dep_time,
    endTime = arr_time,
    endTimeZoneOffset = -330,
    eventCategory = "FLIGHT",
    eventType = "FLIGHT",
    startTimeZoneOffset = 0,
    )
    DutyEventItem(dutyevent = exampleFlight)
    }

@Preview (showBackground = true)
@Composable
fun showRES(){
    val dep_time = DateConverter.convertToTimestamp("2024-04-19T12:00:01Z")
    val arr_time = DateConverter.convertToTimestamp("2024-04-19T18:00:00Z")
    val day = DateConverter.convertfromDateStamp("2024-04-19Z")


    val exampleRES = DutyEvent(
        day = day,
        _links = null,
        endLocation = "JFK",
        startLocation = "FRA",
        wholeDay = true,
        eventAttributes = null,
        eventDetails = "RES",
        startTime = null,
        endTime = null,
        endTimeZoneOffset = -330,
        eventCategory = "GROUNDEVENT",
        eventType = "GROUNDEVENT",
        startTimeZoneOffset = 0,
    )
    DutyEventItem(dutyevent = exampleRES)
}