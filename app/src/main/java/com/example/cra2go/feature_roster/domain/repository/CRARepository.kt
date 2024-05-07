package com.example.cra2go.feature_roster.domain.repository

import com.example.cra2go.feature_roster.domain.utils.DutySchedule
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CRARepository {

    @GET("/v1/flight_operations/crew_services/mock/COMMON_DUTY_EVENTS")
    suspend fun getSchedule(@Query("fromDate") fromDate: String,
                            @Query("toDate") toDate: String,
                            @Query("access_token") token: String)
    : Response<DutySchedule>

    fun handleResponse(response:String)
}