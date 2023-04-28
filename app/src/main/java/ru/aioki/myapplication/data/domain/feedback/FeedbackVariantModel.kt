package ru.aioki.myapplication.data.domain.feedback

import com.aioki.api.client.models.feedback.FeedbackVariantApiModel
import ru.aioki.myapplication.data.domain.SelectableItem

/**
 * Вариант ответа для вопроса в отзыве
 *
 * @param value значение ответа (не для отображения)
 * @param description описание ответа (для отображения)
 * */
data class FeedbackVariantModel(
    val value: String,
    val description: String?,
) : SelectableItem {
    override val id: Int
        get() = value.hashCode()
}

// ------------------------- DOMAIN model -------------------------
fun FeedbackVariantModel.toApi(): FeedbackVariantApiModel {
    return FeedbackVariantApiModel(
        value = value,
        description = description,
    )
}
// ------------------------- DOMAIN model -------------------------

// ------------------------- API model -------------------------
fun FeedbackVariantApiModel.toDomain(): FeedbackVariantModel {
    return FeedbackVariantModel(
        value = value,
        description = description,
    )
}
// ------------------------- API model -------------------------
