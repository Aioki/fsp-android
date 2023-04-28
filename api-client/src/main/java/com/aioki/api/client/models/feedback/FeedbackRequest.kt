package com.aioki.api.client.models.feedback

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedbackRequest(
    @Json(name = ORDER_ID) private val orderId: Int,
    @Json(name = ANSWERS) private val answers: List<FeedbackVariantApiModel>,
    @Json(name = FEEDBACK) private val feedback: String,
) {
    companion object {
        const val ORDER_ID = "orderId"
        const val ANSWERS = "answers"
        const val FEEDBACK = "feedback"
    }
}
