package com.aioki.api.client.models.feedback

import com.aioki.api.client.models.BaseResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedbackResponse(
    @Json(name = QUESTIONS) val questions: List<FeedbackQuestionApiModel>
): BaseResponse() {
    companion object {
        const val QUESTIONS = "questions"
    }
}
