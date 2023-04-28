package ru.aioki.myapplication.data.domain.feedback

import com.aioki.api.client.models.feedback.FeedbackQuestionApiModel

/**
 * Вопрос отзыва
 *
 * @param id идентификатор вопроса
 * @param title заголовок
 * @param answers список ответов
 * */
data class FeedbackQuestionModel(
    val id: Int,
    val title: String,
    val answers: List<FeedbackVariantModel>?,
)

// ------------------------- API model -------------------------
private fun FeedbackQuestionApiModel.toDomain(): FeedbackQuestionModel {
    return FeedbackQuestionModel(
        id = id,
        title = title,
        answers = answers?.map {
            it.toDomain()
        },
    )
}

fun FeedbackQuestionApiModel.toFeedbackStep(): FeedbackStep {
    return when (type) {
        "score" -> FeedbackStep.Rating(
            question = toDomain()
        )

        "binary", "specific" -> FeedbackStep.Choice(
            question = toDomain()
        )

        "text" -> FeedbackStep.Comment(
            question = toDomain()
        )

        else -> FeedbackStep.Undefined
    }
}
// ------------------------- API model -------------------------