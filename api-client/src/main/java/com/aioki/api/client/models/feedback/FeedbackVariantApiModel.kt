package com.aioki.api.client.models.feedback

import com.squareup.moshi.JsonClass

/**
 * Вариант ответа для вопроса в отзыве
 *
 * @param value значение ответа (не для отображения)
 * @param description описание ответа (для отображения)
 * */
@JsonClass(generateAdapter = true)
data class FeedbackVariantApiModel(
    val value: String,
    val description: String?,
) {
    companion object {
        const val value = "VALUE"
        const val description = "DESCRIPTION"
    }
}
