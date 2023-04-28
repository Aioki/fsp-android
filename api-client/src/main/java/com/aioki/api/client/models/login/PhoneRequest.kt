package com.aioki.api.client.models.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhoneRequest(
    @Json(name = PHONE) val phone: String
) {

    companion object {

        const val PHONE = "phone"
    }
}