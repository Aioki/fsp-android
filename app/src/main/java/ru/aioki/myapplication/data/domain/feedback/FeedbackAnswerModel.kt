package ru.aioki.myapplication.data.domain.feedback

import com.aioki.api.client.models.feedback.FeedbackAnswerApiModel

/**
 * Модель ответа на вопрос
 *
 * @param questionId - идентификатор вопроса
 * @param answerId - идентификатор ответа
 * @param value - значение ответа
 * */
data class FeedbackAnswerModel(
    val questionId: Int,
    val answerId: Int?,
    val value: String,
)

// ------------------------- DOMAIN model -------------------------
fun FeedbackAnswerModel.toApi(): FeedbackAnswerApiModel {
    return FeedbackAnswerApiModel(
        questionId = questionId,
        value = value
    )
}
// ------------------------- DOMAIN model -------------------------

// ------------------------- API model -------------------------

// ------------------------- API model -------------------------

