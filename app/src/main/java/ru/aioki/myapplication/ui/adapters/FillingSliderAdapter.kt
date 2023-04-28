package ru.aioki.myapplication.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import ru.aioki.myapplication.R
import ru.aioki.myapplication.databinding.ItemFillingSliderElementBinding

class FillingSliderAdapter : RecyclerView.Adapter<FillingSliderAdapter.FillingSliderViewHolder>() {

    /**
     * Размер слайдера (кол-во делений)
     * */
    private var fillingSliderSize: Int = 0

    /**
     * Позиция, до которой слайдер заполнен (включительно)
     * */
    private var lastFilledPosition: Int = 0

    /**
     * Цвет заполненного деления слайдера
     * */
    @ColorRes
    private var filledColor = FILLED_COLOR

    /**
     * Цвет незаполненного деления слайдера
     * */
    @ColorRes
    private var unfilledColor = UNFILLED_COLOR

    /**
     * Установить цвет заполненного деления слайдера
     * */
    fun setFilledColor(@ColorRes color: Int) {
        filledColor = color
    }

    /**
     * Получить цвет заполненного деления слайдера
     * */
    @ColorRes
    fun getFilledColor(): Int {
        return filledColor
    }

    /**
     * Установить цвет незаполненного деления слайдера
     * */
    fun setUnfilledColor(@ColorRes color: Int) {
        unfilledColor = color
    }

    /**
     * Получить цвет незаполненного деления слайдера
     * */
    @ColorRes
    fun getUnfilledColor(): Int {
        return unfilledColor
    }

    /**
     * Установить размер слайдера (кол-во делений)
     * */
    fun setSliderSize(value: Int) {
        fillingSliderSize = value
        notifyItemRangeChanged(0, value)
    }

    /**
     * Установить позицию, до которой слайдер должен быть заполнен (включительно)
     * */
    /*
    * Принцип работы:
    * - Запоминаем индекс последнего заполненного деления
    * - Устанавливаем новый индекс заполненного деления
    * - Берём меньшее значение, т.е. индекс левого края
    * - Высчитываем длину отрезка, который необходимо обновить
    * - Обновляем только необходимый отрезок
    *
    *   0   1   2   3   4
    *   _   _   -   -   -
    *
    * > Выбран следующий слайд, т.е. нужно обновить слайдер (setLastFilledPosition(2))
    *   savedPos = 1
    *   lastFilledPos = 2
    *
    *   startPosition = 1 (т.к. savedPos < lastFilledPos)
    *   countToUpdate = 2 (т.к. |savedPos - lastFilledPos| + 1)
    *
    *   Обновляем список деления от 1 до 2-го индекса
    *   0   1   2   3   4
    *   _   _   _   -   -
    *   _____________________________________________
    *
    * > Выбран предыдущий слайд, т.е. нужно обновить слайдер (setLastFilledPosition(1))
    *   savedPos = 2
    *   lastFilledPos = 1
    *
    *   startPosition = 1 (т.к. lastFilledPos < savedPos)
    *   countToUpdate = 2 (т.к. |savedPos - lastFilledPos| + 1)
    *
    *   Обновляем список деления от 1 до 2-го индекса
    *   _____________________________________________
    * */
    fun setLastFilledPosition(value: Int) {
        //Сохранить позицию, до которой слайдер был заполнен
        val savedPosition = lastFilledPosition
        //Установить новую позицию, до которой слайдер должен быть заполнен (включительно)
        lastFilledPosition = value

        /*
        * Из двух позиций, до которой слайдер заполнен (которая была и которую установили), выбираем
        * меньшее число для передачи его в notify range
        * */
        val startPosition = savedPosition.takeIf { it < lastFilledPosition } ?: lastFilledPosition
        /*
        * Высчитываем кол-во делений для обновления.
        *
        * В конце добавляется 1, т.к. нам нужно расстояние от левого края (включительно)
        * до правого (включительно)
        *
        * Например, нужно обновить элементы с 0 индекса до 2‑го -> 2 - 0 = 2, поэтому, чтобы обновить
        * все 3 элемента, добавляем 1
        * */
        val countToUpdate = kotlin.math.abs(savedPosition - lastFilledPosition) + 1

        /*
        * Обновляем изменённые деления
        * */
        notifyItemRangeChanged(startPosition, countToUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FillingSliderViewHolder {
        return FillingSliderViewHolder(
            ItemFillingSliderElementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FillingSliderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return fillingSliderSize
    }

    inner class FillingSliderViewHolder(private val binding: ItemFillingSliderElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val color = FILLED_COLOR.takeIf {
                    position <= lastFilledPosition
                } ?: UNFILLED_COLOR

                vElement.backgroundTintList = ColorStateList.valueOf(
                    ResourcesCompat.getColor(
                        root.resources,
                        color,
                        null
                    )
                )
            }
        }
    }

    companion object {
        /**
         * Цвет заполненного деления по умолчанию
         * */
        @ColorRes
        val FILLED_COLOR = R.color.red1

        /**
         * Цвет незаполненного деления по умолчанию
         * */
        @ColorRes
        val UNFILLED_COLOR = R.color.grey8
    }
}