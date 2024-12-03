package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.TestEvents

import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.domain.utils.DateConverter
import org.bmstudio.cra2go.feature_roster.domain.utils.EventAttributes

object TestEvents {
    val dep_time = DateConverter.convertToTimestamp("2024-12-21T02:00:00Z")
    val arr_time = DateConverter.convertToTimestamp("2024-12-21T10:00:00Z")
    val day = DateConverter.convertfromDateStamp("2024-12-21Z")

    val test_rotation: EventAttributes = EventAttributes(
        rotationId = "1",
        dayOfShift = 1
    )

    val exampleFlight: DutyEvent = DutyEvent(
        day = day,
        _links = null,
        endLocation = "PVG",
        startLocation = "FRA",
        wholeDay = false,
        eventAttributes = test_rotation,
        eventDetails = "LH8000",
        startTime = dep_time,
        endTime = arr_time,
        endTimeZoneOffset = -360,
        eventCategory = "flight",
        eventType = "FLIGHT",
        startTimeZoneOffset = -120,
    )

    val exampleFlight2: DutyEvent = DutyEvent(
        day = DateConverter.convertfromDateStamp("2024-12-22Z"),
        _links = null,
        endLocation = "DWC",
        startLocation = "PVG",
        wholeDay = false,
        eventAttributes = test_rotation,
        eventDetails = "LH8001",
        startTime = DateConverter.convertToTimestamp("2024-12-22T20:00:00Z"),
        endTime = DateConverter.convertToTimestamp("2024-12-23T08:00:00Z"),
        endTimeZoneOffset = -360,
        eventCategory = "flight",
        eventType = "FLIGHT",
        startTimeZoneOffset = -120,
    )

    val exampleFlight3: DutyEvent = DutyEvent(
        day = DateConverter.convertfromDateStamp("2024-12-25Z"),
        _links = null,
        endLocation = "PVG",
        startLocation = "FRA",
        wholeDay = false,
        eventAttributes = test_rotation,
        eventDetails = "LH8001",
        startTime = DateConverter.convertToTimestamp("2024-12-25T08:00:00Z"),
        endTime = DateConverter.convertToTimestamp("2024-12-25T12:00:00Z"),
        endTimeZoneOffset = -360,
        eventCategory = "flight",
        eventType = "X",
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
        exampleFlight3
    )

}

