package org.bmstudio.cra2go.feature_roster.domain.repository

import org.bmstudio.cra2go.BuildConfig
import org.bmstudio.cra2go.feature_roster.domain.utils.DutySchedule
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CRARepository {

    @GET(BuildConfig.RESOURCEURL)
    suspend fun getSchedule(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("access_token") token: String
    )
            : Response<DutySchedule>

    fun handleResponse(response: String)
}