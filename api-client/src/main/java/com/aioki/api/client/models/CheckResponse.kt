package com.aioki.api.client.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckResponse(
    val success: Boolean,
    val message: String?,
) : BaseResponse()