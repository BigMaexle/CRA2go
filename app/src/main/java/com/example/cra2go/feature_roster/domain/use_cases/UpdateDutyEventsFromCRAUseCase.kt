package com.example.cra2go.feature_roster.domain.use_cases

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.cra2go.feature_roster.domain.model.DutyEvent
import com.example.cra2go.feature_roster.domain.repository.CRARepository
import com.example.cra2go.feature_roster.domain.repository.DutyEventRepository
import com.example.cra2go.feature_roster.domain.utils.DateConverter
import com.example.cra2go.feature_roster.domain.utils.DutySchedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Scope
import kotlin.coroutines.coroutineContext
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.log


class UpdateDutyEventsFromCRAUseCase (
    private var repository: DutyEventRepository,
    private var craRepository: CRARepository,
) {

    val TAG: String = "UPDATE"

    suspend operator fun invoke(token: String, currentdate: Date){

        val sdf = SimpleDateFormat("yyyy-MM-dd'Z'")
        val c: Calendar = Calendar.getInstance()
        c.setTime(currentdate)

        val fromdate = sdf.format(c.time)

        c.add(Calendar.DATE, 30) // number of days to add

        val todate = sdf.format(c.time)

        val response = try {
            craRepository.getSchedule(fromdate,todate,token)
        } catch(e: IOException) {
            Log.e(TAG, "IOException, you might not have internet connection")
            return
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException, unexpected response")
            return
        }
        if(response.isSuccessful && response.body() != null) {
            Log.i(TAG,"Response successful")
            insertCRADutyEvents(response.body()!!)
        } else {
            Log.e(TAG, "Response not successful")
        }

    }

    suspend fun insertCRADutyEvents(schedule: DutySchedule){

        Log.i(TAG,"Inserting Duty Events")




        Log.i(TAG,"collected")

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


        } }

        val dbevents = repository.getDutyEvents().first()

        Log.i(TAG,dbevents.toString())

        Log.i(TAG,list_of_new_events.toString())



        val earliestDate = list_of_new_events.minByOrNull { it.day }

        Log.i(TAG,earliestDate?.day.toString())

        //DELETE ALL EVENTS WHICH ARE NEWER THAN CRA
        dbevents.onEach { event ->

            Log.i(TAG,event.day.toString())

            if (event.day >= earliestDate?.day){

                repository.deleteDutyEvent(event)

            }



        }

        //NEW EVENTS
        list_of_new_events.onEach { event ->

            repository.insertDutyEvent(event)

        }



    }
}