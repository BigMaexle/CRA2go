package com.example.cra2go.feature_roster.data.repository

import android.icu.util.GregorianCalendar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.example.cra2go.feature_roster.data.data_source.DutyEventDao
import com.example.cra2go.feature_roster.domain.model.DutyEvent
import com.example.cra2go.feature_roster.domain.repository.CRARepository
import com.example.cra2go.feature_roster.domain.utils.DateConverter
import com.example.cra2go.feature_roster.domain.utils.DutySchedule
import com.example.cra2go.feature_roster.domain.utils.RosterDay
import com.google.gson.Gson
import java.util.Date
import java.util.Scanner


class CRARepositoryImp(
    private val dao : DutyEventDao,
    private val queue :RequestQueue
): CRARepository {

    var gson: Gson = Gson()
    override fun getSchedule(url:String) {


        var craresponse : String
        var dutySchedule: DutySchedule

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->

                craresponse = response
                dutySchedule = gson.fromJson(craresponse,DutySchedule::class.java)


                val listoffdays : List<RosterDay> = dutySchedule.rosterDays
                listoffdays.onEach { day ->

                   day.events.onEach { event ->
                           val flight: DutyEvent = DutyEvent(
                               _links = event._links,
                               startTimeZoneOffset = event.startTimeZoneOffset,
                               endTimeZoneOffset = event.endTimeZoneOffset,
                               startTime = DateConverter.convertToTimestamp(event.startTime),
                               endTime = DateConverter.convertToTimestamp(event.endTime),
                               eventAttributes = event.eventAttributes,
                               eventCategory = event.eventCategory,
                               endLocation = event.endLocation,
                               eventType = event.eventType,
                               startLocation = event.startLocation,
                               eventDetails = event.eventDetails,
                               wholeDay = event.wholeDay
                           )
                           dao.insertDutyEvent(flight)


                   }

                }


            },
            { println("That didn't work!")}
        )

// Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

    fun testfun(){
        println()
    }

}