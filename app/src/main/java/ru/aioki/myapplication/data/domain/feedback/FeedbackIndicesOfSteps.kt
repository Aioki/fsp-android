package ru.aioki.myapplication.data.domain.feedback

/**
 * @param stepIndex - индекс текущего шага
 * @param total - всего шагов в числовой последовательности
 * */
data class FeedbackIndicesOfSteps(
    val stepIndex: Int = 0,
    val total: Int = 0,
)
