package com.aioki.api.client.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FileApiModel(
    @Json(name = ID) val id: Int, // 0
    @Json(name = MIME) val mime: String, // string
    @Json(name = PATH) val url: String // string
) {
    companion object {
        const val ID = "id"
        const val MIME = "mime"
        const val PATH = "path"
    }
}