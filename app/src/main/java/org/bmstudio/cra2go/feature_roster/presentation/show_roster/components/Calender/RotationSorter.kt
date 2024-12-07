package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import org.bmstudio.cra2go.feature_roster.domain.model.DisplayEvent
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import java.util.Calendar

fun groupEventsByRotationID(events: List<DutyEvent>): List<List<DisplayEvent>> {
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

    //Now i have grouped DutyEventLists

    val dRotList = mutableListOf(mutableListOf<DisplayEvent>())

    groupedEvents.forEach { rot ->

        //create DisplayEventList
        val dRotation = mutableListOf<DisplayEvent>()
        rot.forEach { dutyevent ->
            val displayEvent = createDisplayEventFromDutyEvent(dutyevent)
            if (displayEvent != null) {
                dRotation.add(displayEvent)
            }
        }

        if(dRotation.isNotEmpty()){
            dRotList.add(dRotation)
        }

    }

    return dRotList

}

fun createDisplayEventFromDutyEvent(dutyevent: DutyEvent): DisplayEvent? {

    if (dutyevent.wholeDay == true){
        //WHOLE DAY EVENT, create a DisplayEvent
        if (dutyevent.day == null) return null

        val c_start = Calendar.getInstance()
        c_start.time = dutyevent.day
        c_start.set(Calendar.HOUR_OF_DAY,0)
        c_start.set(Calendar.MINUTE,0)

        val c_end = Calendar.getInstance()
        c_start.time = dutyevent.day
        c_start.set(Calendar.HOUR_OF_DAY,23)
        c_start.set(Calendar.MINUTE,59)



    }


    return null
}
