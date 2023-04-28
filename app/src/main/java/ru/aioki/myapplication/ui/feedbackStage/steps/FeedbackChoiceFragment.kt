package ru.aioki.myapplication.ui.feedbackStage.steps

import android.util.Log
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseFragment
import ru.aioki.myapplication.data.domain.feedback.FeedbackAnswerModel
import ru.aioki.myapplication.data.domain.feedback.FeedbackStep
import ru.aioki.myapplication.databinding.FragmentFeedbackChoiceBinding
import ru.aioki.myapplication.ui.feedbackStage.FeedbackViewModel
import ru.aioki.myapplication.ui.feedbackStage.adapters.SelectFeedbackVariantAdapter
import ru.aioki.myapplication.utils.observeOnce

@AndroidEntryPoint
class FeedbackChoiceFragment : BaseFragment<FragmentFeedbackChoiceBinding>() {

    private val viewModel: FeedbackViewModel by activityViewModels()

    private var selectFeedbackVariantAdapter: SelectFeedbackVariantAdapter? = null

    override fun getLayoutID(): Int = R.layout.fragment_feedback_choice

    override fun setUpViews() {
        super.setUpViews()

        selectFeedbackVariantAdapter = SelectFeedbackVariantAdapter()

        binding.apply {
            rvVariants.adapter = selectFeedbackVariantAdapter
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.currentStep.observeOnce(viewLifecycleOwner) {
            if (it is FeedbackStep.Choice) {
                Log.e(TAG, "${it.savedAnswer}")

                selectFeedbackVariantAdapter?.apply {
                    submitList(it.question.answers)

                    it.savedAnswer?.answerId?.let { answerId ->
                        setLastSuccessfullySelectedItem(answerId)
                    }

                    setOnSelectListener { variant ->
                        it.savedAnswer = FeedbackAnswerModel(
                            questionId = it.question.id,
                            answerId = variant.id,
                            value = variant.value,
                        )
                    }
                }

                binding.apply {
                    tvHeader.text = it.question.title
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

    override fun onDestroyView() {
        super.onDestroyView()
        selectFeedbackVariantAdapter = null
    }

    companion object {
        private const val TAG = "FeedbackChoiceFragment"
    }
}