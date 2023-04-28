package ru.aioki.myapplication.data.domain

import com.aioki.api.client.models.EventApiModel

data class Event(
    val name: String,
    val desc: String,
    val type: EventType? = null,
    val address: String,
    val startDate: String,
    val endDate: String? = null,
    val maxTeamCount: Int = 0,
    val isPartner: Boolean = false,
) {

    val hasTeamLimit: Boolean
        get() = maxTeamCount > 0

    companion object {
        enum class EventType(val text: String) {
            ALG("Алгоритмическое программирование"),
            HACK("Продуктовое программирование");

            companion object {
                fun fromData(text: String): EventType? {
                    return EventType.values().firstOrNull { it.text == text }
                }
            }
        }
    }
}

fun EventApiModel.toDomain(): Event {
    return Event(
        name = name,
        desc = description,
        type = Event.Companion.EventType.fromData(eventType.name),
        address = address,
        startDate = startDate,
        endDate = endDate,
        maxTeamCount = teamsCountLimit,
        isPartner = isPartner,

        )
}
