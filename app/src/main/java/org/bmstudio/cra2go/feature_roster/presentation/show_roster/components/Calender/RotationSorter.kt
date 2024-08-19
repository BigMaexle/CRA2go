package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent

fun groupEventsByRotationID(events: List<DutyEvent>): List<List<DutyEvent>> {
    val groupedEvents = events
        .filter { it.eventAttributes?.rotationId != null }
        .groupBy { it.eventAttributes?.rotationId }
        .values
        .map { eventList ->
            eventList.sortedBy { it.startTime }
        }
        .toMutableList()

    // Handle events with null RotationID
    val eventsWithoutRotationID = events.filter { it.eventAttributes?.rotationId == null }
    eventsWithoutRotationID.forEach { event ->
        groupedEvents.add(listOf(event))
    }

    return groupedEvents
}