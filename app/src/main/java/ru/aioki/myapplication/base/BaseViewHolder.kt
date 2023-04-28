package ru.aioki.myapplication.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(data: T) {}

    open fun bind(data: T, position: Int) {}
}