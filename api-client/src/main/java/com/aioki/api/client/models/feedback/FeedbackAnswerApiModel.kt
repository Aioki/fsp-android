package com.aioki.api.client.models.feedback

/**
 * Модель ответа на вопрос
 *
 * @param questionId - идентификатор вопроса
 * @param value - значение ответа
 * */
data class FeedbackAnswerApiModel(
    val questionId: Int,
    val value: String,
)
