package org.bmstudio.cra2go.feature_roster.domain.repository

import kotlinx.coroutines.flow.Flow
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent

interface DutyEventRepository {

    fun getDutyEvents(): Flow<List<DutyEvent>>
    suspend fun getDutyEventByID(id: Int): DutyEvent?

    suspend fun insertDutyEvent(dutyEvent: DutyEvent)

    suspend fun deleteDutyEvent(dutyEvent: DutyEvent)

}