package com.aioki.api.client.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class BasePagingResponse : BaseResponse() {

    @Json(name = META)
    var meta: Pagination? = null

    companion object {
        const val META = "pagination"
    }

}