package org.bmstudio.cra2go.feature_roster.domain.use_cases

import android.util.Log
import kotlinx.coroutines.flow.first
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.domain.repository.CRARepository
import org.bmstudio.cra2go.feature_roster.domain.repository.DutyEventRepository
import org.bmstudio.cra2go.feature_roster.domain.utils.DateConverter
import org.bmstudio.cra2go.feature_roster.domain.utils.DutySchedule
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class UpdateDutyEventsFromCRAUseCase(
    private var repository: DutyEventRepository,
    private var craRepository: CRARepository,
) {

    val TAG: String = "UPDATE"

    suspend operator fun invoke(token: String, currentdate: Date) {

        val sdf = SimpleDateFormat("yyyy-MM-dd'Z'")
        val c: Calendar = Calendar.getInstance()
        c.setTime(currentdate)
        c.set(Calendar.DAY_OF_MONTH,1)
        val fromdate = sdf.format(c.time)
        c.setTime(currentdate)

        val todate = sdf.format(getEndOfNextMonth(c.time))

        val response = try {
            Log.i(TAG,"trying to recieve Duty Plan from CRA")
            craRepository.getSchedule(fromdate, todate, token)
        } catch (e: IOException) {
            Log.e(TAG, "IOException, you might not have internet connection")
            return
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException, unexpected response")
            return
        } catch (e: Exception) {
            Log.e(TAG, "Something wrong")
            Log.e(TAG,e.cause.toString())
            Log.e(TAG,e.message.toString())
            return
        }
        if (response.isSuccessful && response.body() != null) {
            Log.i(TAG, "Response successful")
            insertCRADutyEvents(response.body()!!)
        } else {
            Log.e(TAG, "Response not successful")
        }

    }

    suspend fun insertCRADutyEvents(schedule: DutySchedule) {

        schedule.rosterDays.onEach { day ->
            day.events.onEach { event ->
                Log.i(TAG,event.toString())
            }
        }
        Log.i(TAG, "Inserting Duty Events")




        Log.i(TAG, "collected")

        val list_of_new_events = ArrayList<DutyEvent>()
        schedule.rosterDays.onEach { rosterday ->
            val day = rosterday.day

            rosterday.events.onEach { it ->

                var new_event = DutyEvent(
                    day = DateConverter.convertfromDateStamp(day),
                    endTime = DateConverter.convertToTimestamp(it.endTime),
                    startTime = DateConverter.convertToTimestamp(it.startTime),
                    startLocation = it.startLocation,
                    endLocation = it.endLocation,
                    _links = it._links,
                    eventAttributes = it.eventAttributes,
                    eventCategory = it.eventCategory,
                    eventDetails = it.eventDetails,
                    endTimeZoneOffset = it.endTimeZoneOffset,
                    eventType = it.eventType,
                    wholeDay = it.wholeDay,
                    startTimeZoneOffset = it.startTimeZoneOffset
                )

                list_of_new_events.add(new_event)


            }
        }

        val dbevents = repository.getDutyEvents().first()

        Log.i(TAG, dbevents.toString())

        Log.i(TAG, list_of_new_events.toString())


        val earliestDate = list_of_new_events.minByOrNull { it.day }

        Log.i(TAG, earliestDate?.day.toString())

        //DELETE ALL EVENTS WHICH ARE NEWER THAN CRA
        dbevents.onEach { event ->

            Log.i(TAG, event.day.toString())

            if (event.day >= earliestDate?.day) {

                repository.deleteDutyEvent(event)

            }


        }

        //NEW EVENTS
        list_of_new_events.onEach { event ->

            repository.insertDutyEvent(event)

        }


    }

    private fun getEndOfNextMonth(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date

        // Add one month
        calendar.add(Calendar.MONTH, 1)

        // Set the day to the last day of the month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))

        return calendar.time
    }
}