package com.example.cra2go.feature_roster.domain.use_cases

data class DutyEventUseCases(
    val getDutyEvents: GetDutyEventsUseCase,
    val deleteDutyEvent: DeleteDutyEventUseCase,
    val updateDutyEventsFromCRA: UpdateDutyEventsFromCRAUseCase
)
