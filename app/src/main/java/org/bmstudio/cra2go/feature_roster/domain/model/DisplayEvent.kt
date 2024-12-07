package org.bmstudio.cra2go.feature_roster.domain.model

import java.util.Date

//Helper Class
//Derived from Duty Event, but it is null-asserted
//To be used to display events in CalenderMode

data class DisplayEvent (
    val startTime: Date,
    val endTime: Date,
    val startLocation: String,
    val endLocation: String,
    val category: String
)