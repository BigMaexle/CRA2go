package com.example.cra2go.feature_roster.domain.repository

import com.android.volley.toolbox.Volley
import com.example.cra2go.feature_roster.domain.utils.DutySchedule

interface CRARepository {

    fun getSchedule(url:String)
}