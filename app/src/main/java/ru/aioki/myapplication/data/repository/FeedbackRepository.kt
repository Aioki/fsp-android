package ru.aioki.myapplication.data.repository

import com.aioki.api.client.models.feedback.FeedbackQuestionApiModel
import com.aioki.api.client.models.feedback.FeedbackVariantApiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.aioki.myapplication.data.datasource.FeedbackDataSource
import ru.aioki.myapplication.data.domain.feedback.FeedbackStep
import ru.aioki.myapplication.data.domain.feedback.toFeedbackStep
import ru.aioki.myapplication.utils.Resource
import javax.inject.Inject

class FeedbackRepository @Inject constructor(
    private val feedbackDataSource: FeedbackDataSource,
) {
    suspend fun getFeedbackSteps(): Resource<List<FeedbackStep>> =
        withContext(Dispatchers.IO) {

            delay(1000)

            val list = listOf<FeedbackQuestionApiModel>(
                FeedbackQuestionApiModel(
                    id = 1111,
                    title = "test1",
                    type = "test1",
                    answers = null,
                ),
                FeedbackQuestionApiModel(
                    id = 3548,
                    title = "Оцените мероприятие?",
                    type = "score",
                    answers = listOf(
                        FeedbackVariantApiModel(
                            value = "1",
                            description = "Неудовлетворительно",
                        ),
                        FeedbackVariantApiModel(
                            value = "2",
                            description = "Удовлетворительно",
                        ),
                        FeedbackVariantApiModel(
                            value = "3",
                            description = "Хорошо",
                        ),
                        FeedbackVariantApiModel(
                            value = "4",
                            description = "Отлично",
                        ),
                    ),
                ),
                FeedbackQuestionApiModel(
                    id = 3547,
                    title = "Остались ли вы довольны качеством организации?",
                    type = "specific",
                    answers = listOf(
                        FeedbackVariantApiModel(
                            value = "2673",
                            description = "Маловероятно",
                        ),
                        FeedbackVariantApiModel(
                            value = "2674",
                            description = "Скорее нет, чем да",
                        ),
                        FeedbackVariantApiModel(
                            value = "2675",
                            description = "Вероятно",
                        ),
                        FeedbackVariantApiModel(
                            value = "2676",
                            description = "Скорее да, чем нет",
                        ),
                        FeedbackVariantApiModel(
                            value = "2677",
                            description = "Несомненно",
                        ),
                    ),
                ),
                FeedbackQuestionApiModel(
                    id = 2222,
                    title = "test2",
                    type = "test2",
                    answers = null,
                ),
                FeedbackQuestionApiModel(
                    id = 3885,
                    title = "Если вам что-то не понравилось, расскажите нам об этом",
                    type = "text",
                    answers = null,
                ),
            )

            val domainList = list.map {
                it.toFeedbackStep()
            }.filter {
                it != FeedbackStep.Undefined
            }

            val finalList = mutableListOf<FeedbackStep>().apply {
                addAll(domainList)
                add(FeedbackStep.FinalScreen)
            }

            return@withContext Resource.success(
                finalList
            )
        }

    suspend fun sendFeedback(): Resource<Unit> {
        return withContext(IO) {
            TODO()
        }
    }
}