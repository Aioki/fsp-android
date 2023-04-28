package ru.aioki.myapplication.ui.feedbackStage.steps

import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseFragment
import ru.aioki.myapplication.databinding.FragmentFeedbackRatingBinding
import ru.aioki.myapplication.ui.feedbackStage.FeedbackViewModel

@AndroidEntryPoint
class FeedbackFinalScreenFragment : BaseFragment<FragmentFeedbackRatingBinding>() {

    private val viewModel: FeedbackViewModel by activityViewModels()

    override fun getLayoutID(): Int = R.layout.fragment_feedback_final_screen

    companion object {
        private const val TAG = "FeedbackFinalScreenFrag"
    }
}