package com.aioki.api.client.models.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationApiModel(
    @Json(name = SALES) val sales: Boolean?,
    @Json(name = HITS) val hits: Boolean?,
    @Json(name = NEWS) val news: Boolean?,
) {
    companion object {
        const val SALES = "sales"
        const val HITS = "hits"
        const val NEWS = "news"
    }
}
