package ru.aioki.myapplication.ui.feedbackStage.steps

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseFragment
import ru.aioki.myapplication.data.domain.feedback.FeedbackAnswerModel
import ru.aioki.myapplication.data.domain.feedback.FeedbackStep
import ru.aioki.myapplication.databinding.FragmentFeedbackCommentBinding
import ru.aioki.myapplication.ui.feedbackStage.FeedbackViewModel
import ru.aioki.myapplication.utils.observeOnce

@AndroidEntryPoint
class FeedbackCommentFragment : BaseFragment<FragmentFeedbackCommentBinding>() {

    private val viewModel: FeedbackViewModel by activityViewModels()

    override fun getLayoutID(): Int = R.layout.fragment_feedback_comment

    override fun observeData() {
        super.observeData()

        viewModel.currentStep.observeOnce(viewLifecycleOwner) {
            if (it is FeedbackStep.Comment) {
                binding.apply {
                    tvHeader.text = it.question.title

                    etComment.setText(it.savedAnswer?.value)

                    binding.apply {
                        etComment.addTextChangedListener { comment ->
                            it.savedAnswer = FeedbackAnswerModel(
                                questionId = it.question.id,
                                answerId = null,
                                value = comment.toString()
                            )
                        }
                    }
                }
            } else {
                throw IllegalStateException(FeedbackViewModel.WRONG_EXPECTED_TYPE)
            }
        }

        viewModel.stepSliderState.observe(viewLifecycleOwner) {
            if (it is FeedbackViewModel.Companion.StepSliderState.Visible) {
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
        private const val TAG = "FeedbackCommentFragment"
    }
}