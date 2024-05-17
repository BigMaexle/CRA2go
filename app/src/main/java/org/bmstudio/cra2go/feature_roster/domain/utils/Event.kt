package org.bmstudio.cra2go.feature_roster.domain.utils

data class Event(
    val _links: Links,
    val endLocation: String,
    val endTime: String,
    val endTimeZoneOffset: Int,
    val eventAttributes: EventAttributes,
    val eventCategory: String,
    val eventDetails: String,
    val eventType: String,
    val startLocation: String,
    val startTime: String,
    val startTimeZoneOffset: Int,
    val wholeDay: Boolean
)