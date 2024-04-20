package com.example.cra2go.feature_roster.domain.utils

data class DutySchedule(
    val fromDate: String,
    val pkNumber: String,
    val rosterDays: List<RosterDay>,
    val toDate: String
)