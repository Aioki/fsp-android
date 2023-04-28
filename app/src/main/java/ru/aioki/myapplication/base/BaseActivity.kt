package ru.aioki.myapplication.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.databinding.ViewDataBinding
import ru.aioki.myapplication.utils.doOnApplyWindowInsets

abstract class BaseActivity<VB : ViewDataBinding>(
    val bindingFactory: (LayoutInflater) -> VB
) : AppCompatActivity() {

    private var mViewBinding: VB? = null
    val binding get() = mViewBinding!!

    abstract fun getFragmentContainer(): Int

    /**
     * Нужен для переходов, в этот момент binding == null!!!!
     * */

    @CallSuper
    open fun configureActivity() {
    }

    @CallSuper
    open fun parseArguments() {
    }

    @CallSuper
    open fun setUpView() {
        applyInsets()
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val decorView = window.decorView
        window.decorView.systemUiVisibility =
            decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }

    /**Добавить инсет на АКТИВИТИ (фрагменты имеет свои инсеты)*/
    open fun applyInsets() {
        binding.root.doOnApplyWindowInsets { view, insets, padding ->
            view.updatePadding(
                //Устанавливается инсет на высоту вызванной клавиатуры
                bottom = padding.bottom + insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            )
            insets
        }
    }

    @CallSuper
    open fun observeData() {
    }

    @CallSuper
    open fun observeView() {
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureActivity()
        mViewBinding = bindingFactory(layoutInflater)
        parseArguments()
        setUpView()
        observeView()
        observeData()
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewBinding = null
    }

}