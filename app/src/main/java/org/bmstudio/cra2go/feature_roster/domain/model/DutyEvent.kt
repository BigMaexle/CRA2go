package org.bmstudio.cra2go.feature_roster.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bmstudio.cra2go.feature_roster.domain.utils.EventAttributes
import org.bmstudio.cra2go.feature_roster.domain.utils.Links
import java.util.Date


@Entity
data class DutyEvent(
    val day: Date,
    val _links: Links?,
    val endLocation: String?,
    val endTime: Date?,
    val endTimeZoneOffset: Int?,
    val eventAttributes: EventAttributes?,
    val eventCategory: String?,
    val eventDetails: String?,
    val eventType: String?,
    val startLocation: String?,
    val startTime: Date?,
    val startTimeZoneOffset: Int?,
    val wholeDay: Boolean?,
    @PrimaryKey val id: Int? = null
) {
}