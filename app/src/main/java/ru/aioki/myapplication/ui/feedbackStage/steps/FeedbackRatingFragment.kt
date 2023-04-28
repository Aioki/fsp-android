package ru.aioki.myapplication.ui.feedbackStage.steps

import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseFragment
import ru.aioki.myapplication.data.domain.feedback.FeedbackAnswerModel
import ru.aioki.myapplication.data.domain.feedback.FeedbackStep
import ru.aioki.myapplication.databinding.FragmentFeedbackRatingBinding
import ru.aioki.myapplication.ui.feedbackStage.FeedbackViewModel
import ru.aioki.myapplication.ui.feedbackStage.FeedbackViewModel.Companion.StepSliderState
import ru.aioki.myapplication.utils.observeOnce

@AndroidEntryPoint
class FeedbackRatingFragment : BaseFragment<FragmentFeedbackRatingBinding>() {

    private val viewModel: FeedbackViewModel by activityViewModels()

    override fun getLayoutID(): Int = R.layout.fragment_feedback_rating

    override fun observeData() {
        super.observeData()

        viewModel.currentStep.observeOnce(viewLifecycleOwner) {
            if (it is FeedbackStep.Rating) {
                binding.apply {
                    tvHeader.text = it.question.title

                    srbRating.rating = it.savedAnswer?.value?.toFloat() ?: 0.0f

                    srbRating.setOnRatingChangeListener { ratingBar, rating, fromUser ->
                        it.savedAnswer = FeedbackAnswerModel(
                            questionId = it.question.id,
                            answerId = null,
                            value = rating.toString()
                        )
                    }
                }
            } else {
                throw IllegalStateException(FeedbackViewModel.WRONG_EXPECTED_TYPE)
            }
        }

        viewModel.stepSliderState.observe(viewLifecycleOwner) {
            if (it is StepSliderState.Visible) {
                binding.apply {
                    tvStepNumberOutOfTotal.text = getString(
                        R.string.StepNumberOutOfTotal,
                        it.stepIndex + 1,
                        it.totalSteps,
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "FeedbackRatingFragment"
    }
}