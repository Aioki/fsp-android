package ru.aioki.myapplication.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import ru.aioki.myapplication.ui.models.Error
import ru.aioki.myapplication.utils.CodeThrottle
import ru.aioki.myapplication.utils.THROTTLE_TIME_MS
import ru.aioki.myapplication.utils.doOnApplyWindowInsets

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    private var mViewBinding: VB? = null
    val binding get() = mViewBinding!!

    abstract fun getLayoutID(): Int

    protected val codeThrottle = CodeThrottle(THROTTLE_TIME_MS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mViewBinding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        observeView()
        observeData()
    }

    @CallSuper
    open fun setUpViews() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    @CallSuper
    open fun observeView() {
    }

    @CallSuper
    open fun observeData() {
    }

    @CallSuper
    open fun parseArguments() {
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        mViewBinding = null
    }

    protected fun applyInsets(v: View) {
        v.doOnApplyWindowInsets { view, insets, padding ->
            view.updatePadding(
                top = padding.top + insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            )
            insets
        }
    }


//    internal fun logNotify(@StringRes message: Int) =
//        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

    internal fun logNotify(message: String?) {
//        Snackbar.make(binding.root, message?: "Произошла ошибка", Snackbar.LENGTH_SHORT).show()
        Toast.makeText(binding.root.context, message ?: "Произошла ошибка", Toast.LENGTH_SHORT)
            .show()
        Log.d("notify", message.toString())
    }

    /**
     * Устанавливает ошибки в поля
     * @param fields Мапа полей (key - имя поля, value - поле, в которое устанавливается ошибка)
     * @param errors Мапа ошибок (key - имя поля, value - список ошибок)
     */
    open fun showErrorsInFields(fields: Map<String, Error>?, errors: Map<String, Array<String>>?) {
        errors?.forEach { (fieldName, errors) ->
            fields?.get(fieldName)?.setError(errors.firstOrNull())
        }
    }
}