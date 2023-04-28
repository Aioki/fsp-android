package ru.aioki.myapplication.ui.feedbackStage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseSelectAdapter
import ru.aioki.myapplication.base.BaseViewHolder
import ru.aioki.myapplication.data.domain.feedback.FeedbackVariantModel
import ru.aioki.myapplication.databinding.ItemFeedbackButtonSelectableBinding

class SelectFeedbackVariantAdapter : BaseSelectAdapter<FeedbackVariantModel>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<FeedbackVariantModel> {
        return SelectFeedbackVariantViewHolder(
            ItemFeedbackButtonSelectableBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<FeedbackVariantModel>, position: Int) {
        holder.bind(currentList[position], position)
    }

    private inner class SelectFeedbackVariantViewHolder(
        private val binding: ItemFeedbackButtonSelectableBinding
    ) : BaseViewHolder<FeedbackVariantModel>(binding.root) {
        override fun bind(data: FeedbackVariantModel, position: Int) {
            val backgroundColor = FeedbackVariantButtonColor.Selected.backgroundColor.takeIf {
                position == lastSuccessfullySelectedPos
            } ?: FeedbackVariantButtonColor.Unselected.backgroundColor

            val textColor = FeedbackVariantButtonColor.Selected.textColor.takeIf {
                position == lastSuccessfullySelectedPos
            } ?: FeedbackVariantButtonColor.Unselected.textColor

            binding.apply {
                tvTitle.text = data.description

                tvTitle.setTextColor(
                    ResourcesCompat.getColor(
                        binding.root.resources,
                        textColor,
                        null
                    )
                )

                cvRoot.setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        binding.root.resources,
                        backgroundColor,
                        null
                    )
                )

                cvRoot.setOnClickListener {
                    select(position)
                }
            }
        }
    }

    private sealed class FeedbackVariantButtonColor {

        /**
         * Цвета выбранного элемента
         * */
        object Unselected : FeedbackVariantButtonColor() {
            override val backgroundColor: Int = R.color.grey7
            override val textColor: Int = R.color.white
        }

        /**
         * Цвета невыбранного элемента
         * */
        object Selected : FeedbackVariantButtonColor() {
            override val backgroundColor: Int = R.color.grey8
            override val textColor: Int = R.color.black
        }

        /**
         * Цвет фона
         * */
        @get:ColorRes
        abstract val backgroundColor: Int

        /**
         * Цвет текста
         * */
        @get:ColorRes
        abstract val textColor: Int
    }
}