package org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.Calender

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.bmstudio.cra2go.feature_roster.domain.model.DisplayEvent
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.presentation.show_roster.components.TestEvents.TestEvents
import org.bmstudio.cra2go.ui.theme.CRA2goTheme
import java.util.Calendar
import java.util.Date
import kotlin.math.abs

@Composable
fun CalendarGrid(events: List<DutyEvent>, currentMonth: Calendar,
                 selectedDateEvents: MutableState<List<DutyEvent>>,
                 selectedDay: MutableState<Calendar>) {



    val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfMonth = currentMonth.clone() as Calendar
    firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
    val startingDayOfWeek = determineDay(firstDayOfMonth)
    val days = (1..daysInMonth).toList()
    val paddingDaysBefore = (-startingDayOfWeek + 1 .. 0).toList()
    val paddingDaysAfter = List((7 - (startingDayOfWeek + daysInMonth) % 7) % 7) { -1 }
    val allDays = paddingDaysBefore + days + paddingDaysAfter

    val DisplayEventList = createDisplayEventList(events,currentMonth)

    LazyVerticalGrid(
        columns = GridCells.Fixed(7)
    ) {
        items(allDays.size) { index ->

            val day = allDays[index]

            val eventsForDay = events.filter { event ->
                val eventcal = Calendar.getInstance().apply { time = event.day }
                val dayofmonth = Calendar.getInstance().apply { time = firstDayOfMonth.time }
                    .apply { add(Calendar.DAY_OF_MONTH, day) }

                //compare if the event day is the same as the day of the month
                eventcal.get(Calendar.YEAR) == dayofmonth.get(Calendar.YEAR) &&
                        eventcal.get(Calendar.MONTH) == dayofmonth.get(Calendar.MONTH) &&
                        eventcal.get(Calendar.DAY_OF_MONTH) == dayofmonth.get(Calendar.DAY_OF_MONTH)
            }



            val events_to_be_displayed = find_display_events_for_day(day+1,currentMonth,DisplayEventList)


            DayCell(
                currentMonth = currentMonth,
                day = day,
                eventsForDay = eventsForDay,
                selectedEvents = selectedDateEvents,
                selectedDay = selectedDay,
                activeDutyEvents = events_to_be_displayed
            )

        }
    }
}

fun find_display_events_for_day(day: Int, currentMonth: Calendar, displayEventList: List<List<List<DisplayEvent>>>): List<List<DisplayEvent>> {


    val maximumdays = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val minimumdays = currentMonth.getActualMinimum(Calendar.DAY_OF_MONTH)

    if (day < minimumdays-1 || day > maximumdays-1) {
        //DayCell is not in active Month (PaddingDays)
        return listOf(listOf<DisplayEvent>())
    }

    return displayEventList[day]

}

fun createDisplayEventList(events: List<DutyEvent>, currentMonth: Calendar): List<List<List<DisplayEvent>>> {

    //THIS IS WHERE THE MAGIC HAPPENS.
    //Input: Random List of DutyEvents and the current Month

    // Output: A List of events to be displayed.
    // Step 1. Sort all DutyEvents to Rotations, to be displayed as one -> rotations: List<DisplayEvents>
    // All Rotations that begin at Day index are in List so e[index] = List<rotation>
    // All in a List e

    // Step one,
    // Take a List: DUTYEVENTS create a List:DISPLAYEVENTS rotations = same rotationID, if you have no rotID, you are single Event

    val filtered_for_month_events: List<DutyEvent> = filter_events_for_month(events,currentMonth)

    val groupedDutyEvents = groupEventsByRotationID(filtered_for_month_events)

    val groupedDisplayEvents: List<List<DisplayEvent>> = convert_Duty_Event_List_to_Display_Event_List(groupedDutyEvents)

    // unorganizedListofDisplayEvents - Cut them according SO-MO or end of Month or Begin of Month

    val croppedDisplayEvents: List<List<DisplayEvent>> = crop_Display_Event_List (groupedDisplayEvents,currentMonth)

    val days_in_month = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val indexed_Display_Rotation = List<List<List<DisplayEvent>>>(days_in_month){ index: Int -> get_Events_starting_at_Day_Of_Month(index,currentMonth,croppedDisplayEvents) }

    return indexed_Display_Rotation

}

fun crop_Display_Event_List(
    groupedDisplayEvents: List<List<DisplayEvent>>,
    currentMonth: Calendar
): List<List<DisplayEvent>> {

    //Crop Rotation according to Mondays, End Of Month or Begin of Month

    val crop_points: List<Date> = create_crop_points(currentMonth)

    val cropped_list = mutableListOf<List<DisplayEvent>>()

    groupedDisplayEvents.forEach { event ->

        val cropped_events:List<List<DisplayEvent>> = crop_rotation(event,crop_points)

        cropped_events.forEach { cropped_rot ->
            cropped_list.add(cropped_rot)
        }

    }

    Log.i("ListCropper",cropped_list.toString())
    print("test")


    return cropped_list

}

fun crop_rotation(rotation: List<DisplayEvent>, cropPoints: List<Date>): List<List<DisplayEvent>> {
    val cutted_rotations = mutableListOf<List<DisplayEvent>>()

    cutted_rotations.add(rotation)

    cropPoints.forEach { point ->

        if (spans_over(cutted_rotations.last(),point)){



            val cutted_rot:Pair<List<DisplayEvent>,List<DisplayEvent>> = cut_rotation(cutted_rotations.last(),point)
            cutted_rotations.remove(cutted_rotations.last())
            cutted_rotations.add(cutted_rot.first)
            cutted_rotations.add(cutted_rot.second)



        }

    }


    return cutted_rotations.toList()

}

fun cut_rotation(
    unsorted_rotation: List<DisplayEvent>,
    point: Date
): Pair<List<DisplayEvent>, List<DisplayEvent>> {

    val rotation = unsorted_rotation.sortedBy { it.startTime }

    val before = mutableListOf<DisplayEvent>()
    val after = mutableListOf<DisplayEvent>()

    //Sort Events to before or after crop point - if overlaps, divide it
    rotation.forEach { event ->

        if(event.startTime < point){
            if (event.endTime < point){
                before.add(event)
            }

            if (event.endTime > point){

                val cropped_before = DisplayEvent(
                    startTime = event.startTime,
                    endTime = point,
                    category = event.category,
                    startLocation = event.startLocation,
                    endLocation = event.endLocation
                )

                val adj_cal = Calendar.getInstance()
                adj_cal.time = point
                adj_cal.add(Calendar.MINUTE,2)

                val cropped_after = DisplayEvent(
                    startTime = adj_cal.time,
                    endTime = event.endTime,
                    category = event.category,
                    startLocation = event.startLocation,
                    endLocation = event.endLocation
                )

                before.add(cropped_before)
                after.add(cropped_after)

            }

        }

        if (event.startTime > point){
            after.add(event)
        }

    }

    //instert dummyEvent to show where the point is
    if(before.last().endTime != point){
        before.add(
            DisplayEvent(
            startTime = point,
            endTime = point,
            category = "dummy",
            startLocation = before.last().endLocation,
            endLocation = before.last().endLocation,
        )
        )
    }

    val adj_cal = Calendar.getInstance()
    adj_cal.time = point
    adj_cal.add(Calendar.MINUTE,2)

    if(after.last().startTime != point){
        after.add(DisplayEvent(
            startTime = adj_cal.time,
            endTime = adj_cal.time,
            category = "dummy",
            startLocation = before.last().endLocation,
            endLocation = before.last().endLocation,
        ))
    }

    val before_sorted = before.sortedBy { it.startTime }
    val after_sorted = after.sortedBy { it.startTime }

    return Pair(before_sorted,after_sorted)

}

fun spans_over(rotation: List<DisplayEvent>, point: Date): Boolean {

    return rotation.first().startTime < point && rotation.last().endTime > point

}

fun create_crop_points(currentMonth: Calendar): List<Date> {
    val points = mutableListOf<Date>()

    val crop_cal = currentMonth

    //Add firstof Month
    crop_cal.set(Calendar.DAY_OF_MONTH,1)
    crop_cal.set(Calendar.HOUR_OF_DAY,0)
    crop_cal.set(Calendar.MINUTE,0)

    points.add(crop_cal.time)

    //Add lasttof Month
    crop_cal.set(Calendar.DAY_OF_MONTH,currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
    crop_cal.set(Calendar.HOUR_OF_DAY,23)
    crop_cal.set(Calendar.MINUTE,59)

    points.add(crop_cal.time)

    crop_cal.set(Calendar.MONTH,currentMonth.get(Calendar.MONTH)+1)

    //Add Mondays

    for (d in (0..<(currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)))){

        crop_cal.set(Calendar.DAY_OF_MONTH,d)

        if (crop_cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){

            crop_cal.set(Calendar.DAY_OF_MONTH,d)
            crop_cal.set(Calendar.HOUR_OF_DAY,23)
            crop_cal.set(Calendar.MINUTE,59)

            points.add(crop_cal.time)

        }

    }

    return points.sortedBy { it.time }

}



fun filter_events_for_month(events: List<DutyEvent>, currentMonth: Calendar): List<DutyEvent> {
    val filtered_events = mutableListOf<DutyEvent>()
    events.forEach { event->
        val cal = Calendar.getInstance()
        cal.time = event.day

        if (isInMonthOrAdjacentMonth(cal,currentMonth)){
            filtered_events.add(event)
        }

    }
    return filtered_events
}

fun isInMonthOrAdjacentMonth(cal: Calendar, currentMonth: Calendar): Boolean {

    val test_cal = cal

    if (test_cal.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR) &&
        test_cal.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH)) return true
    test_cal.add(Calendar.MONTH,1)
    if (test_cal.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR) &&
        test_cal.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH)) return true
    test_cal.add(Calendar.MONTH,-2)
    if (test_cal.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR) &&
        test_cal.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH)) return true

    return false
}

fun get_Events_starting_at_Day_Of_Month(dayOfMonth: Int,month: Calendar, groupedevents: List<List<DisplayEvent>>): List<List<DisplayEvent>> {

    val events_starting_at_day = mutableListOf<List<DisplayEvent>>()

    groupedevents.forEach { rot ->
        val rot_start_cal = Calendar.getInstance()
        rot_start_cal.time = rot.first().startTime

        if(rot_start_cal.get(Calendar.DAY_OF_MONTH)  == dayOfMonth &&
            rot_start_cal.get(Calendar.MONTH) == month.get(Calendar.MONTH) &&
            rot_start_cal.get(Calendar.YEAR) == month.get(Calendar.YEAR)){
            events_starting_at_day.add(rot)
        }

    }

    return events_starting_at_day

}

fun convert_Duty_Event_List_to_Display_Event_List(groupedDutyEvents: List<List<DutyEvent>>): List<List<DisplayEvent>> {



    val groupedDisplayEvent = mutableListOf<List<DisplayEvent>>()

    groupedDutyEvents.forEach { dutyRotation ->

        val displayRotation = mutableListOf<DisplayEvent>()

        dutyRotation.forEach { event ->

            val converted_display_event: DisplayEvent? = convert_Duty_Event_To_Display_Event(event)

            if (converted_display_event != null){
                displayRotation.add(converted_display_event)
            }

        }

        if(displayRotation.isNotEmpty()){
            val sortedDisplayRotation = displayRotation.sortedBy { it.startTime }
            groupedDisplayEvent.add(sortedDisplayRotation)
        }


    }

    return groupedDisplayEvent

}

fun convert_Duty_Event_To_Display_Event(event: DutyEvent): DisplayEvent? {

    val TAG = "Duty->Display Converter"

    if (event.eventDetails == "X") return null

    val cal = Calendar.getInstance()
    cal.time.time = event.day.time

    val category: String = determine_display_category(event)

    if (event.wholeDay == true) {

        val start_time_cal = Calendar.getInstance()
        start_time_cal.time = event.day
        start_time_cal.set(Calendar.HOUR_OF_DAY,0)
        start_time_cal.set(Calendar.MINUTE,0)

        val end_time_cal = Calendar.getInstance()
        end_time_cal.time = event.day
        end_time_cal.set(Calendar.HOUR_OF_DAY,23)
        end_time_cal.set(Calendar.MINUTE,59)

        val displayEvent = DisplayEvent(
            startTime = start_time_cal.time,
            endTime = end_time_cal.time,
            category = category,
            endLocation = "",
            startLocation = "",
        )

        return displayEvent

    }

    if (event.eventType == "FLIGHT") {
        if (event.startTime == null || event.endTime == null) {
            Log.w(TAG,"invalid Start/EndTimes of Flight")
            return null
        }
        if (event.endLocation == null || event.startLocation == null){
            Log.w(TAG,"invalid Start/EndTimes of Flight")
            return null
        }

        val displayEvent = DisplayEvent(
            startTime = event.startTime,
            endTime = event.endTime,
            category = category,
            endLocation = event.endLocation,
            startLocation = event.startLocation,
        )


        return displayEvent

    }

    if (event.eventType == "GROUNDEVENT"){
        if (event.startTime == null || event.endTime == null) {
            Log.w(TAG,"invalid Start/EndTimes of Flight")
            return null
        }
        if (event.endLocation == null || event.startLocation == null){
            Log.w(TAG,"invalid Start/EndTimes of Flight")
            return null
        }

        val displayEvent = DisplayEvent(
            startTime = event.startTime,
            endTime = event.endTime,
            category = category,
            endLocation = "",
            startLocation = "",
        )


        return displayEvent
    }

    Log.w(TAG,"unknown Type of Event")
    return null


}

fun determine_display_category(event: DutyEvent): String {
    return "default"
}

fun isOnCurrentDay(event: DutyEvent, dayofmonth: Calendar): Boolean {

    if (event.startTime != null){
        val eventcal = Calendar.getInstance().apply { time = event.startTime }
        if (eventcal.get(Calendar.YEAR) == dayofmonth.get(Calendar.YEAR) &&
            eventcal.get(Calendar.MONTH) == dayofmonth.get(Calendar.MONTH) &&
            eventcal.get(Calendar.DAY_OF_MONTH) == dayofmonth.get(Calendar.DAY_OF_MONTH)
        ){ return true}
    }
    if (event.endTime != null){
        val eventcal = Calendar.getInstance().apply { time = event.endTime }
        if (eventcal.get(Calendar.YEAR) == dayofmonth.get(Calendar.YEAR) &&
            eventcal.get(Calendar.MONTH) == dayofmonth.get(Calendar.MONTH) &&
            eventcal.get(Calendar.DAY_OF_MONTH) == dayofmonth.get(Calendar.DAY_OF_MONTH)
        ){ return true}
    }
    if (event.day != null){
        val eventcal = Calendar.getInstance().apply { time = event.day }
        if (eventcal.get(Calendar.YEAR) == dayofmonth.get(Calendar.YEAR) &&
            eventcal.get(Calendar.MONTH) == dayofmonth.get(Calendar.MONTH) &&
            eventcal.get(Calendar.DAY_OF_MONTH) == dayofmonth.get(Calendar.DAY_OF_MONTH)
        ){ return true}
    }

    return false

}

fun determineDay(firstDayOfMonth: Calendar): Int {
    return if (firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1 == 0) {
        7
    } else{
        firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1
    }
}

@Preview (showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun CalendarGridPreview() {

    val testcal = Calendar.getInstance()
    testcal.set(Calendar.MONTH,Calendar.DECEMBER)

    CRA2goTheme {
        CalendarGrid(events = TestEvents.exampleRotation, currentMonth = testcal,
            selectedDateEvents = remember { mutableStateOf(listOf()) },
            selectedDay = remember { mutableStateOf(Calendar.getInstance()) })
        }
}
