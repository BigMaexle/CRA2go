package com.example.cra2go.feature_roster.domain.use_cases

import com.example.cra2go.feature_roster.domain.model.DutyEvent
import com.example.cra2go.feature_roster.domain.repository.DutyEventRepository

class DeleteDutyEventUseCase(
    private val repository: DutyEventRepository
) {

    suspend operator fun invoke(dutyevent : DutyEvent){
        repository.deleteDutyEvent(dutyevent)
    }
}