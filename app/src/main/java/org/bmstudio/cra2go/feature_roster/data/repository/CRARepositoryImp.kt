package org.bmstudio.cra2go.feature_roster.data.repository

import android.os.Build
import android.util.Log
import org.bmstudio.cra2go.BuildConfig
import org.bmstudio.cra2go.feature_roster.data.data_source.DutyEventDao
import org.bmstudio.cra2go.feature_roster.domain.repository.CRARepository
import org.bmstudio.cra2go.feature_roster.domain.utils.DutySchedule
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CRARepositoryImp(
    private val dao: DutyEventDao,
) : CRARepository {

    val TAG: String = "CRAREPO"

    val rf = Retrofit.Builder()
        .baseUrl(BuildConfig.BASEURLAPI)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CRARepository::class.java)

    override suspend fun getSchedule(
        fromDate: String,
        toDate: String,
        token: String
    ): Response<DutySchedule> {

        val response = rf.getSchedule(fromDate, toDate, token)
        return response

    }

    override fun handleResponse(response: String) {
        println("nix")
    }
}