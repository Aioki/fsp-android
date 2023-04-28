package ru.aioki.myapplication.data.domain.feedback

import androidx.fragment.app.Fragment
import ru.aioki.myapplication.ui.feedbackStage.steps.FeedbackChoiceFragment
import ru.aioki.myapplication.ui.feedbackStage.steps.FeedbackCommentFragment
import ru.aioki.myapplication.ui.feedbackStage.steps.FeedbackFinalScreenFragment
import ru.aioki.myapplication.ui.feedbackStage.steps.FeedbackRatingFragment
import kotlin.reflect.KClass

/**
 * Указывает, что шаг должен иметь фрагмент для отображения и быть (не)включенным в последовательность
 * */
interface FeedbackFragment {
    /**
     * Класс фрагмента
     * */
    val fragmentType: KClass<out Fragment>

    /**
     * Фрагмент включен в список последовательность шагов (1 из 5, 2 из 5,...) или является
     * отдельным фрагментом
     * */
    val includedInSequence: Boolean
}

/**
 * Указывает, что шаг должен иметь ответ
 * */
interface FeedbackAnswer {
    /**
     * Сохраненный ответ
     * */
    var savedAnswer: FeedbackAnswerModel?
}

sealed class FeedbackStep {
    object Undefined : FeedbackStep()

    object Finish : FeedbackStep()

    object FinalScreen : FeedbackStep(),
        FeedbackFragment {
        override val fragmentType: KClass<out Fragment> = FeedbackFinalScreenFragment::class

        override val includedInSequence: Boolean = false
    }

    data class Rating(
        val question: FeedbackQuestionModel,
    ) : FeedbackStep(),
        FeedbackFragment,
        FeedbackAnswer {
        override val fragmentType: KClass<out Fragment> = FeedbackRatingFragment::class

        override val includedInSequence: Boolean = true

        override var savedAnswer: FeedbackAnswerModel? = null
    }

    data class Choice(
        val question: FeedbackQuestionModel,
    ) : FeedbackStep(),
        FeedbackFragment,
        FeedbackAnswer {
        override val fragmentType: KClass<out Fragment> = FeedbackChoiceFragment::class

        override val includedInSequence: Boolean = true

        override var savedAnswer: FeedbackAnswerModel? = null
    }

    data class Comment(
        val question: FeedbackQuestionModel,
    ) : FeedbackStep(),
        FeedbackFragment,
        FeedbackAnswer {
        override val fragmentType: KClass<out Fragment> = FeedbackCommentFragment::class

        override val includedInSequence: Boolean = true

        override var savedAnswer: FeedbackAnswerModel? = null
    }

    companion object {
        /**
         * Шаг включен в последовательность шагов отзыва (1 из 5, 2 из 5, ...)
         * */
        fun isStepIncludedInSequence(step: FeedbackStep): Boolean {
            return if (step is FeedbackFragment) {
                step.includedInSequence
            } else {
                false
            }
        }
    }
}
