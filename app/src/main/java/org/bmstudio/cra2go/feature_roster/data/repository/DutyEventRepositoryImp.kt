package org.bmstudio.cra2go.feature_roster.data.repository

import kotlinx.coroutines.flow.Flow
import org.bmstudio.cra2go.feature_roster.data.data_source.DutyEventDao
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.domain.repository.DutyEventRepository

class DutyEventRepositoryImp(
    private val dao: DutyEventDao
) : DutyEventRepository {
    override fun getDutyEvents(): Flow<List<DutyEvent>> {
        return dao.getDutyEvents()
    }

    override suspend fun getDutyEventByID(id: Int): DutyEvent? {
        return dao.getDutyEventByID(id)
    }

    override suspend fun insertDutyEvent(dutyEvent: DutyEvent) {
        return dao.insertDutyEvent(dutyEvent)
    }

    override suspend fun deleteDutyEvent(dutyEvent: DutyEvent) {
        dao.deleteDutyEvent(dutyEvent)
    }
}