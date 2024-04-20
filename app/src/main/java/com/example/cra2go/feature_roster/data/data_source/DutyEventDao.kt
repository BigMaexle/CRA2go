package com.example.cra2go.feature_roster.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cra2go.feature_roster.domain.model.DutyEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface DutyEventDao {

    @Query("SELECT * FROM dutyEvent")
    fun getDutyEvents() : Flow<List<DutyEvent>>

    @Query("SELECT * FROM dutyevent WHERE id = :id")
    fun getDutyEventByID(id: Int): DutyEvent?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDutyEvent(dutyEvent : DutyEvent)

    @Delete
    fun deleteDutyEvent(dutyEvent: DutyEvent)
}