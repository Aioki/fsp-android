package ru.aioki.myapplication.ui.feedbackStage

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseActivity
import ru.aioki.myapplication.customView.FillingSliderViewItem
import ru.aioki.myapplication.data.domain.feedback.FeedbackFragment
import ru.aioki.myapplication.data.domain.feedback.FeedbackStep
import ru.aioki.myapplication.databinding.ActivityFeedbackBinding
import ru.aioki.myapplication.ui.feedbackStage.FeedbackViewModel.Companion.ActionButtonState
import ru.aioki.myapplication.ui.feedbackStage.FeedbackViewModel.Companion.StepSliderState
import ru.aioki.myapplication.utils.Resource.Status
import ru.aioki.myapplication.utils.doOnApplyWindowInsets
import kotlin.reflect.full.createInstance

@AndroidEntryPoint
class FeedbackActivity : BaseActivity<ActivityFeedbackBinding>(ActivityFeedbackBinding::inflate) {

    private val viewModel: FeedbackViewModel by viewModels()

    /** Ползунок для заполнения */
    private var fillingSliderViewItem: FillingSliderViewItem? = null

    override fun getFragmentContainer(): Int = R.id.nav_host_fragment_activity_feedback

    override fun configureActivity() {
        super.configureActivity()

        intent?.apply {
            val defaultValue = -1
            getIntExtra(ORDER_ID, defaultValue).let {
                if (it == defaultValue) {
                    throw IllegalStateException("Order id wasn't passed")
                } else {
                    viewModel.setOrderId(it)
                }
            }
        }
    }

    override fun setUpView() {
        super.setUpView()

        //Установка слайдера заполнения
        fillingSliderViewItem = FillingSliderViewItem(binding.viewItemFillingSlider).apply {
            setVerticalDivider(null)
        }
    }

    override fun observeView() {
        super.observeView()

        binding.btnAction.setOnClickListener {
            viewModel.executeStepAction()
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.loadingState.observe(this) {
            /*
            * В дальнейшем при it.status == Status.Error можно в currentStep установить экран
            * с ошибкой, а на кнопку "обновить" запросить данные снова
            * */
            binding.apply {
                pbLoading.isVisible = it.status == Status.LOADING
                groupContent.isVisible = it.status != Status.LOADING
            }

            when (it.status) {
                Status.SUCCESS -> {

                }

                Status.LOADING -> {

                }

                Status.ERROR, Status.NETWORK_ERROR -> {
                    val message = it.errorBody?.message
                        ?: getString(R.string.failed_get_order_feedback_data)

                    Toast.makeText(
                        this,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.currentStep.observe(this) {
            Log.d(TAG, "currentStep: $it")

            when (it) {
                is FeedbackStep.Finish -> {
                    finish()
                }

                is FeedbackFragment -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(getFragmentContainer(), it.fragmentType.createInstance())
                        commitNow()
                    }
                }

                else -> {}
            }
        }

        viewModel.btnActionTitle.observe(this) { state ->
            binding.btnAction.apply {
                when (state) {
                    is ActionButtonState.Visible -> {
                        text = getString(state.text)
                        isVisible = true
                    }

                    is ActionButtonState.Invisible -> {
                        visibility = View.INVISIBLE
                    }

                    is ActionButtonState.Gone -> {
                        isVisible = false
                    }

                    else -> {}
                }
            }
        }

        viewModel.stepSliderState.observe(this) { state ->
            binding.viewItemFillingSlider.root.apply {
                when (state) {
                    is StepSliderState.Visible -> {
                        isVisible = true

                        fillingSliderViewItem?.apply {
                            setSliderSize(state.totalSteps)
                            setLastFilledPosition(state.stepIndex)
                        }
                    }

                    is StepSliderState.Invisible -> {
                        visibility = View.INVISIBLE
                    }

                    is StepSliderState.Gone -> {
                        isVisible = false
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.stepActionState.collect { res ->
                when (res.status) {
                    Status.SUCCESS -> {
                        // nothing
                    }

                    Status.LOADING -> {
                        // nothing
                    }

                    Status.ERROR -> {
                        val message = res.errorBody?.message
                            ?: "Ошибка на шаге"

                        Toast.makeText(
                            this@FeedbackActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    Status.NETWORK_ERROR -> {}
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fillingSliderViewItem?.clear()
        fillingSliderViewItem = null
    }

    override fun applyInsets() {
        binding.root.doOnApplyWindowInsets { view, insets, padding ->
            view.updatePadding(
                //Устанавливается инсет на высоту вызванной клавиатуры
                top = padding.top + insets.getInsets(WindowInsetsCompat.Type.statusBars()).top,
                bottom = padding.bottom + insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            )
            insets
        }
    }

    override fun onBackPressed() {
        viewModel.goBack()
    }

    companion object {
        private const val TAG = "FeedbackActivity"

        /** Идентификатор заказа */
        private const val ORDER_ID = "feedback.order_id"

        fun getBundle(
            orderId: Int
        ): Bundle {
            return Bundle().apply {
                putInt(ORDER_ID, orderId)
            }
        }
    }
}