package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.domain.utils.DateConverter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


@Composable
fun DutyEventItem(dutyevent: DutyEvent) {


    if (dutyevent.eventDetails == "X") {
        return
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            Modifier.padding(8.dp)
        ) {
            if (dutyevent.eventType == "BRIEFING"){

                val formatter = SimpleDateFormat("HH:mm")
                val c = Calendar.getInstance()
                c.time = dutyevent.startTime!!
                c.add(Calendar.MINUTE,-dutyevent.startTimeZoneOffset!!)
                val lt = c.time

                Text(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(2.dp),
                    text = "Briefing ${dutyevent.startLocation!!} - " +
                            "${formatter.format(lt)} LT",
                    textAlign = TextAlign.Center)

                Divider()
            }
            if (dutyevent.eventType == "FLIGHT") {
                ShowFlightAirportRow(
                    DepAirport = dutyevent.startLocation,
                    ArrAirport = dutyevent.endLocation,
                    flightno = dutyevent.eventDetails
                )
            }
            if (dutyevent.eventType == "GROUNDEVENT") {
                dutyevent.eventDetails?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(20.dp),
                        text = it,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }
            }
            if (dutyevent.eventType == "HOTEL") {
                dutyevent.startLocation?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(20.dp),
                        text = it,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }
            }
            if (dutyevent.wholeDay == false && dutyevent.eventType != "HOTEL" && dutyevent.eventType != "BRIEFING") {


                ShowDutyTimes(
                    startTime = dutyevent.startTime,
                    endTime = dutyevent.endTime,
                    startoffset = dutyevent.startTimeZoneOffset,
                    endoffset = dutyevent.endTimeZoneOffset,
                    dayofevent = dutyevent.day
                )
            }
        }


    }
}


@Composable
fun ShowFlightAirportRow(DepAirport: String?, ArrAirport: String?, flightno: String?) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Departure", fontSize = 9.sp, textAlign = TextAlign.Left)
            if (DepAirport != null) {
                Text(text = DepAirport, textAlign = TextAlign.Left, fontSize = 20.sp)
            }
        }

        flightno?.let {
            Text(
                text = it,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }

        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            Text(text = "Arrival", fontSize = 9.sp, textAlign = TextAlign.Right)
            if (ArrAirport != null) {
                Text(text = ArrAirport, textAlign = TextAlign.End, fontSize = 20.sp)
            }
        }
    }

}


@Composable
fun ShowDutyTimes(startTime: Date?, endTime: Date?, startoffset: Int?, endoffset: Int?,dayofevent: Date) {
    val formatter = SimpleDateFormat("HH:mm")
    val day = Calendar.getInstance()
    day.time = dayofevent

    fun isNextDay(time: Calendar) : Boolean {

        return if (time.get(Calendar.DAY_OF_YEAR) != day.get(Calendar.DAY_OF_YEAR)){
            true
        } else {
            false
        }

    }



    CompositionLocalProvider(
        LocalTextStyle provides TextStyle(fontSize = 14.sp)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                if (startTime != null){
                    val c_start = Calendar.getInstance()
                    c_start.time = startTime
                    val off = formatter.format(c_start.time)
                    Text(text = "$off UTC")
                } else {
                    Text(text = "")}


                



                Spacer(modifier = Modifier.weight(1f))

                if (endTime != null){
                    val c_end = Calendar.getInstance()
                    c_end.time = endTime
                    var on = formatter.format(c_end.time)
                    on = if (isNextDay(c_end)) "$on UTC (+1)" else "$on UTC"
                    Text(text = on)
                }  else{ Text(text = "")}


            }
            CompositionLocalProvider(LocalTextStyle provides TextStyle(fontSize = 12.sp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    
                    if (startTime != null) {
                        val c_start = Calendar.getInstance()
                        c_start.time = startTime
                        if (startoffset != null) {
                            c_start.add(Calendar.MINUTE, -startoffset)
                        }
                        var off = formatter.format(c_start.time)
                        off = if (isNextDay(c_start)) "$off LT (+1)" else "$off LT"
                        Text(text = off)
                    } else {
                        Text(text = "")}


                    Spacer(modifier = Modifier.weight(1f))
                    
                    if (endTime != null){
                        val c_end = Calendar.getInstance()
                        c_end.time = endTime
                        if (endoffset != null) {
                            c_end.add(Calendar.MINUTE, -endoffset)
                        }
                        var on = formatter.format(c_end.time)
                        on = if (isNextDay(c_end)) "$on LT (+1)" else "$on LT"
                        Text(text = on)
                    } else {
                        Text(text = "")}







                }

            }
        }

    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun showExampleFlight() {
    val dep_time = DateConverter.convertToTimestamp("2024-04-19T23:00:00Z")
    val arr_time = DateConverter.convertToTimestamp("2024-04-20T10:00:00Z")
    val day = DateConverter.convertfromDateStamp("2024-04-19Z")

    val exampleFlight: DutyEvent = DutyEvent(
        day = day,
        _links = null,
        endLocation = "PVG",
        startLocation = "FRA",
        wholeDay = false,
        eventAttributes = null,
        eventDetails = "LH8000",
        startTime = dep_time,
        endTime = arr_time,
        endTimeZoneOffset = -360,
        eventCategory = "FLIGHT",
        eventType = "FLIGHT",
        startTimeZoneOffset = -120,
    )
    DutyEventItem(dutyevent = exampleFlight)
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun showRES() {
    val dep_time = DateConverter.convertToTimestamp("2024-04-19T12:00:01Z")
    val arr_time = DateConverter.convertToTimestamp("2024-04-20T04:00:00Z")
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