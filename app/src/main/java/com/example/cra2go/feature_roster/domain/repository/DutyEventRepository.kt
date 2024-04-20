package com.example.cra2go.feature_roster.domain.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.cra2go.feature_roster.domain.model.DutyEvent
import kotlinx.coroutines.flow.Flow

interface DutyEventRepository {

    fun getDutyEvents() : Flow<List<DutyEvent>>
    suspend fun getDutyEventByID(id:Int):DutyEvent?

    suspend fun insertDutyEvent(dutyEvent : DutyEvent)

    suspend fun deleteDutyEvent(dutyEvent: DutyEvent)

}