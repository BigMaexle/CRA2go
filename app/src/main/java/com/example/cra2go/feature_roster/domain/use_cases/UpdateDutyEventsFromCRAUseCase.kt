package com.example.cra2go.feature_roster.domain.use_cases

import com.example.cra2go.classes.FlightEvent
import com.example.cra2go.feature_roster.domain.model.DutyEvent
import com.example.cra2go.feature_roster.domain.repository.CRARepository
import com.example.cra2go.feature_roster.domain.repository.DutyEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.sql.Time
import java.util.Date

class UpdateDutyEventsFromCRAUseCase (
    private var repository: DutyEventRepository,
    private var craRepository: CRARepository
) {

    suspend operator fun invoke(){

        val url: String = "https://api-sandbox.lufthansa.com/v1/flight_operations/crew_services/mock/COMMON_DUTY_EVENTS?fromDate=2017-10-01Z&toDate=2017-11-01Z&" +
                "access_token=92h6fh3g2axaxs5cxrudcjmp"

        craRepository.getSchedule(url)



    }
}