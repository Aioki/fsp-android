package com.aioki.api.client.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pagination(
    val currentPage: Int,
    val pageSize: Int,
    val totalCount: Int,
    val totalPages: Int,
)
