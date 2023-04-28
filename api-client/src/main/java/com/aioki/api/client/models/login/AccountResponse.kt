package com.aioki.api.client.models.login

import com.aioki.api.client.models.BaseResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class AccountResponse(
    @Json(name = TOKEN) val token: String,
    @Json(name = PHONE) val phone: String,
//    @Json(name = PROFILE) val profile: ProfileApiModel,
//    @Json(name = IS_NEW) val isNew: Boolean?
) : BaseResponse() {
    companion object {
        const val TOKEN = "accessToken"
        const val PROFILE = "profile"
        const val IS_NEW = "isNew"
        const val PHONE = "phoneNumber"
    }
}
