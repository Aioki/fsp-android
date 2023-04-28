package com.aioki.api.client.models.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileApiModel(
    @Json(name = ID) val id: Int,
    @Json(name = NAME) val name: String?,
    @Json(name = PHONE) val phone: String?,
    @Json(name = EMAIL) val email: String?,
    @Json(name = BIRTHDAY) val birthday: String?,
    @Json(name = NOTIFICATIONS) val notification: NotificationApiModel?,
) {
    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val PHONE = "phone"
        const val EMAIL = "email"
        const val BIRTHDAY = "birthday"
        const val NOTIFICATIONS = "notifications"
    }
}
