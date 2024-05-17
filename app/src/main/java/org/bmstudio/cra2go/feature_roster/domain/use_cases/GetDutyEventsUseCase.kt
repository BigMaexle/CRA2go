package org.bmstudio.cra2go.feature_roster.domain.use_cases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent
import org.bmstudio.cra2go.feature_roster.domain.repository.DutyEventRepository

class GetDutyEventsUseCase(
    private var repository: DutyEventRepository
) {

    operator fun invoke(): Flow<List<DutyEvent>> {
        return repository.getDutyEvents().map { events -> events.sortedBy { it.day } }
    }
}