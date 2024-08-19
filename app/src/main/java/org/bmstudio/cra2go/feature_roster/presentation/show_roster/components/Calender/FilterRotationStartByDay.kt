package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import java.util.Calendar
import java.util.Date

// Function to filter duty events by day
fun filterRotationStartByDay(day: Date, groupedEvents: List<List<DutyEvent>>): List<List<DutyEvent>> {
    return groupedEvents.filter { eventList ->
        // Sort the events in the list by day
        val earliestEvent = eventList.minByOrNull { it.day }
        // Check if the earliest event's day matches the given day
        val c1 = Calendar.getInstance().apply { time = day }
        val c2 = Calendar.getInstance().apply { time = earliestEvent?.day ?: day }
        c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }
}

// Function to filter duty events by day
fun filterRotationcontinuesByDay(day: Date, groupedEvents: List<List<DutyEvent>>): List<List<DutyEvent>> {
    return groupedEvents.filter { eventList ->
        // Sort the events in the list by day
        val earliestEvent = eventList.minByOrNull { it.day }
        // Check if the earliest event's day matches the given day
        val c1 = Calendar.getInstance().apply { time = day }
        val c2 = Calendar.getInstance().apply { time = earliestEvent?.day ?: day }
        c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }
}