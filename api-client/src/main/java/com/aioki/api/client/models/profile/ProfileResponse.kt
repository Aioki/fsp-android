package com.aioki.api.client.models.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.aioki.api.client.models.BaseResponse

@JsonClass(generateAdapter = true)
data class ProfileResponse(
    @Json(name = PROFILE) val profile: ProfileApiModel
) : BaseResponse() {
    companion object {
        const val PROFILE = "profile"
    }
}
