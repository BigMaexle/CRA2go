package com.example.cra2go.feature_roster.data.repository

import com.example.cra2go.feature_roster.data.data_source.DutyEventDao
import com.example.cra2go.feature_roster.domain.repository.CRARepository
import com.example.cra2go.feature_roster.domain.utils.DutySchedule
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CRARepositoryImp (
    private val dao: DutyEventDao,
    ):CRARepository {

    val rf = Retrofit.Builder()
        .baseUrl("https://api-sandbox.lufthansa.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CRARepository::class.java)
    override suspend fun getSchedule(
        fromDate: String,
        toDate: String,
        token: String
    ): Response<DutySchedule> {

        val response = rf.getSchedule(fromDate,toDate,token)
        return response

    }

    override fun handleResponse(response: String) {
        println("nix")
    }
}