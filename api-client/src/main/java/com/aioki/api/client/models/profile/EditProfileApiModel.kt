package com.aioki.api.client.models.profile

import com.squareup.moshi.Json

data class EditProfileApiModel(
    @Json(name = NAME) val name: String?,
    @Json(name = EMAIL) val email: String?,
    @Json(name = BIRTHDAY) val birthday: String?,
    @Json(name = NOTIFICATIONS) val notification: NotificationApiModel?,
) {
    companion object {
        const val NAME = "name"
        const val EMAIL = "email"
        const val BIRTHDAY = "birthday"
        const val NOTIFICATIONS = "notifications"
    }
}
