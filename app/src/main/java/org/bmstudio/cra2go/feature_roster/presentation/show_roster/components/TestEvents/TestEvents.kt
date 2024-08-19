package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.TestEvents

import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.domain.utils.DateConverter

object TestEvents {
    val dep_time = DateConverter.convertToTimestamp("2024-04-19T10:00:00Z")
    val arr_time = DateConverter.convertToTimestamp("2024-04-19T18:00:00Z")
    val day = DateConverter.convertfromDateStamp("2024-04-19Z")

    val exampleFlight: DutyEvent = DutyEvent(
        day = day,
        _links = null,
        endLocation = "FRA",
        startLocation = "PVG",
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

    val exampleFlight2: DutyEvent = DutyEvent(
        day = DateConverter.convertfromDateStamp("2024-04-21Z"),
        _links = null,
        endLocation = "PVG",
        startLocation = "FRA",
        wholeDay = false,
        eventAttributes = null,
        eventDetails = "LH8001",
        startTime = DateConverter.convertToTimestamp("2024-04-21T10:00:00Z"),
        endTime = DateConverter.convertToTimestamp("2024-04-21T18:00:00Z"),
        endTimeZoneOffset = -360,
        eventCategory = "FLIGHT",
        eventType = "FLIGHT",
        startTimeZoneOffset = -120,
    )


    val exampleSB: DutyEvent = DutyEvent(
        day = day,
        _links = null,
        endLocation = null,
        startLocation = null,
        wholeDay = false,
        eventAttributes = null,
        eventDetails = "LH8000",
        startTime = dep_time,
        endTime = arr_time,
        endTimeZoneOffset = -360,
        eventCategory = "FLIGHT",
        eventType = "GROUNDEVENT",
        startTimeZoneOffset = -120,
    )

    val exampleHotel: DutyEvent = DutyEvent(
        day = DateConverter.convertfromDateStamp("2024-04-20Z"),
        _links = null,
        endLocation = null,
        startLocation = null,
        wholeDay = false,
        eventAttributes = null,
        eventDetails = "LH8000",
        startTime = dep_time,
        endTime = arr_time,
        endTimeZoneOffset = -360,
        eventCategory = "FLIGHT",
        eventType = "GROUNDEVENT",
        startTimeZoneOffset = -120,
    )

    val exampleRotation: List<DutyEvent> = listOf(
        exampleFlight,
        exampleFlight2
    )

}

