package com.aioki.api.client.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhoneApiModel(
    @Json(name = PARSED_PHONE) val parsedPhone: String, // 8 (999) 999-99-99
    @Json(name = RAW_NUMBER) val rawNumber: String // +79999999999
) {
    companion object {
        const val PARSED_PHONE = "parsedPhone"
        const val RAW_NUMBER = "rawNumber"
    }
}