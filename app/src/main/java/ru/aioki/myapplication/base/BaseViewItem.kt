package ru.aioki.myapplication.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseViewItem<VB : ViewDataBinding>(binding: VB) {

    private var mViewBinding: VB? = binding
    val binding get() = mViewBinding!!

    val root
        get() = binding.root

    open fun clear() {
        mViewBinding = null
    }

    companion object {

        inline fun <reified I : BaseViewItem<VB>, reified VB : ViewDataBinding> bind(
            layoutInflater: LayoutInflater,
            @LayoutRes layout: Int
        ): I {
            // ViewGroup для передачи параметров (если parent - null то параметры игнорируются)
            // Было бы неплохо найти другое решение
            val parent: View = layoutInflater.inflate(layout, null)

            val binding = DataBindingUtil.inflate<VB>(
                layoutInflater,
                layout,
                parent as ViewGroup,
                false
            )
            return I::class.java.getConstructor(VB::class.java).newInstance(binding)
        }
    }
}