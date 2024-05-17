package org.bmstudio.cra2go.classes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.bmstudio.cra2go.ui.theme.CRA2goTheme
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


@Preview(showBackground = true)
@Composable
fun ShowSampleFlight() {
    val exampleflight = FlightEvent(
        date = Date(2024, 4, 1),
        offblock = Time(12, 0, 0),
        onblock = Time(15, 0, 0),
        depatureAirport = "FRA",
        destinationAirport = "JFK"
    )
    CRA2goTheme {
    }
}