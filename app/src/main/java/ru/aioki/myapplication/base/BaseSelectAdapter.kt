package ru.aioki.myapplication.base

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.aioki.myapplication.data.domain.SelectableItem

abstract class BaseSelectAdapter<T : SelectableItem> :
    ListAdapter<T, BaseViewHolder<T>>(object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }) {

    /**
     * Действие при выборе элемента
     * */
    private var onSelectListener: ((data: T) -> Unit)? = null

    /**
     * Последняя успешно выбранная позиция
     * */
    protected var lastSuccessfullySelectedPos: Int? = null
        set(value) {
            /*
            * Устанавливает значение, если оно > 0
            * иначе последняя выбранная позиция равно null
            * */
            field = value.takeIf { value != null && value >= 0 }
        }

    /**
     * Установить действие при выборе элемента
     * */
    fun setOnSelectListener(block: (data: T) -> Unit) {
        onSelectListener = block
    }

    /**
     * Установить последний успешно выбранный элемент
     * */
    fun setLastSuccessfullySelectedItem(itemId: Int) {
        if (currentList.isNotEmpty()) {
            lastSuccessfullySelectedPos = currentList.indexOfFirst {
                it.id == itemId
            }
            restoreLastSuccessfullySelectedItem()
        }
    }

    /**
     * Восстановить последний успешно выбранный элемент
     * */
    private fun restoreLastSuccessfullySelectedItem() {
        lastSuccessfullySelectedPos?.let {
            select(it)
        }
    }

    /**
     * Выбрать новый элемент
     *
     * @param newSelectedPos позиция, которую необходимо выбрать
     * */
    protected fun select(newSelectedPos: Int) {
        //Индекс текущего выбранного элемента
        val oldSelectedPos = lastSuccessfullySelectedPos

        //Если выбирается не тот же элемент
        if (newSelectedPos != oldSelectedPos) {
            //Если раньше был выбран какой-то элемент
            if (oldSelectedPos != null) {
                notifyItemChanged(oldSelectedPos)
            }

            //Если новый элемент находится в списке
            if (newSelectedPos in 0 until currentList.size) {
                lastSuccessfullySelectedPos = newSelectedPos

                val newSelectedModel = getItem(newSelectedPos)

                notifyItemChanged(newSelectedPos)

                onSelectListener?.invoke(newSelectedModel)
            }
        }
    }

    @CallSuper
    override fun submitList(list: List<T>?) {
        super.submitList(list)
    }

    @CallSuper
    override fun submitList(list: List<T>?, commitCallback: Runnable?) {
        super.submitList(list, commitCallback)
    }
}