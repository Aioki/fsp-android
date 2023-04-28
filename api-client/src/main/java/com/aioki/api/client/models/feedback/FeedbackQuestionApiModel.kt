package com.aioki.api.client.models.feedback

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Вопрос отзыва
 *
 * @param id идентификатор вопроса
 * @param title заголовок
 * @param type тип вопроса
 * @param answers список ответов
 * */
@JsonClass(generateAdapter = true)
data class FeedbackQuestionApiModel(
    @Json(name = ID) val id: Int,
    @Json(name = TITLE) val title: String,
    @Json(name = TYPE) val type: String,
    @Json(name = ANSWERS) val answers: List<FeedbackVariantApiModel>?,
) {
    companion object {
        const val ID = "id"
        const val TITLE = "title"
        const val TYPE = "type"
        const val ANSWERS = "answers"
    }
}
