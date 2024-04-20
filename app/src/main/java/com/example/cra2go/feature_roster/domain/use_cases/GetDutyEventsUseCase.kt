package com.example.cra2go.feature_roster.domain.use_cases

import com.example.cra2go.feature_roster.domain.model.DutyEvent
import com.example.cra2go.feature_roster.domain.repository.DutyEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDutyEventsUseCase (
    private var repository: DutyEventRepository
){

    operator fun invoke(): Flow<List<DutyEvent>>{
        return repository.getDutyEvents().map { events -> events.sortedBy { it.startTime } }
    }
}