package com.aioki.api.client.models

data class EventApiModel(
    val address: String,
    val description: String,
    val endDate: String,
    val eventType: EventType,
    val isPartner: Boolean,
    val name: String,
    val startDate: String,
    val teamsCountLimit: Int
)