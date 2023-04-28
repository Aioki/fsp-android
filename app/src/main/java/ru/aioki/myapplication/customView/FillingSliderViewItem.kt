package ru.aioki.myapplication.customView

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseViewItem
import ru.aioki.myapplication.databinding.ViewItemFillingSliderBinding
import ru.aioki.myapplication.ui.adapters.FillingSliderAdapter
import ru.aioki.myapplication.utils.removeItemDecorations

class FillingSliderViewItem(
    binding: ViewItemFillingSliderBinding,
) : BaseViewItem<ViewItemFillingSliderBinding>(binding) {

    /** Адаптер слайдера заполнения */
    private var fillingSliderAdapter: FillingSliderAdapter? = null

    /**
     * Разделитель между элементами строки
     * */
    @DrawableRes
    private var columnDivider: Int? = DEFAULT_COLUMN_DIVIDER

    init {
        fillingSliderAdapter = FillingSliderAdapter()

        binding.rvFillingSlider.apply {
            adapter = fillingSliderAdapter

            layoutManager = FlexboxLayoutManager(root.context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.NOWRAP  //Сжимать контент (отключение переноса на новую строку)
                justifyContent = JustifyContent.CENTER
            }

            removeItemDecorations()
        }
    }

    /**
     * Установить размер слайдера (кол-во делений)
     * */
    fun setSliderSize(value: Int) {
        fillingSliderAdapter?.setSliderSize(value)
    }

    /**
     * Установить позицию, до которой слайдер должен быть заполнен (включительно)
     * */
    fun setLastFilledPosition(value: Int) {
        fillingSliderAdapter?.setLastFilledPosition(value)
    }

    /**
     * Установить цвет заполненного деления слайдера
     * */
    fun setFilledColor(@ColorRes color: Int) {
        fillingSliderAdapter?.setFilledColor(color)
    }

    /**
     * Получить цвет заполненного деления слайдера
     * */
    @ColorRes
    fun getFilledColor(): Int {
        return fillingSliderAdapter?.getFilledColor()
            ?: throw IllegalStateException("FillingSliderAdapter has not been initialized")
    }

    /**
     * Установить цвет незаполненного деления слайдера
     * */
    fun setUnfilledColor(@ColorRes color: Int) {
        fillingSliderAdapter?.setUnfilledColor(color)
    }

    /**
     * Получить цвет незаполненного деления слайдера
     * */
    @ColorRes
    fun getUnfilledColor(): Int {
        return fillingSliderAdapter?.getUnfilledColor()
            ?: throw IllegalStateException("FillingSliderAdapter has not been initialized")
    }

    fun setVerticalDivider(@DrawableRes divider: Int?) {
        columnDivider = divider

        setupDecoration()
    }

    override fun clear() {
        super.clear()
        fillingSliderAdapter = null
    }

    private fun setupDecoration() {
        binding.rvFillingSlider.apply {
            //Если columnDivider != null, то установить разделитель между элементами
            columnDivider?.let { divider ->
                addItemDecoration(FlexboxItemDecoration(root.context).apply {
                    setOrientation(FlexboxItemDecoration.VERTICAL)
                    setDrawable(
                        ResourcesCompat.getDrawable(
                            root.resources,
                            divider,
                            null
                        )
                    )
                })
            }
        }
    }

    companion object {
        val DEFAULT_COLUMN_DIVIDER = R.drawable.empty_vertical_divider_10
    }
}