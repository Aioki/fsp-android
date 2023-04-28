package ru.aioki.myapplication.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textfield.TextInputLayout
import ru.aioki.myapplication.R
import java.text.DecimalFormat
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*
import kotlin.math.min

@BindingAdapter("app:errorText")
fun setErrorText(view: TextInputLayout, errorMessage: String?) {
    if (errorMessage.isNullOrEmpty()) {
        view.error = null
        view.isErrorEnabled = false
        view.editText?.background = ContextCompat.getDrawable(view.context, R.drawable.bg_edit_text)
    } else {
        view.error = errorMessage
        view.isErrorEnabled = true
        view.editText?.background =
            ContextCompat.getDrawable(view.context, R.drawable.bg_edit_text_error)
    }
}

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}


/**
 * Удалить все декораторы ресайклера
 * */
fun RecyclerView.removeItemDecorations() {
    while (itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
}

fun View.doOnApplyWindowInsets(block: (View, WindowInsetsCompat, Rect) -> WindowInsetsCompat) {

    val initialPadding = recordInitialPaddingForView(this)

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
    }

    requestApplyInsetsWhenAttached()
}

private fun recordInitialPaddingForView(view: View) =
    Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}


fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.showKeyboard(et: EditText) {
    view?.let { activity?.showKeyboard(et) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, 0)
}

fun Double.toDecimalString(): String {
    return DecimalFormat("###,###.###").format(this)
}

fun TextView.setSupportTextAppearance(context: Context, resId: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        @Suppress("DEPRECATION")
        setTextAppearance(context, resId)
    } else {
        setTextAppearance(resId)
    }
}

/** Функция склонения числительных в русском языке
 *
 *  Например нужно найти склонение для слова "элемент"
 *  в массив слов нужно передать слова по следующему принципу:
 *  Перебираем с 1 и ищем возможные склонения (для нашего примера: 1 элемент, 2 элемента, 5 элементов)
 *  Передаем число (500)
 *  Получаем строку "500 элементов"
 *  @param number число, которое нужно просклонять
 *  @param titles Массив слов для склонения
 *  @return строку с числом и склоненным словом
 * */
fun declToNum(number: Int, titles: List<String>): String {
    val word = titles[if (number % 100 in 5..19) 2 else caseDeclToNum[min(number % 10, 5)]]
    return "$number $word"
}

val caseDeclToNum = arrayOf(2, 0, 1, 1, 1, 2)


fun View.openUrl(urlString: String) {
    val viewIntent = Intent(
        "android.intent.action.VIEW",
        Uri.parse(urlString)
    )
    ContextCompat.startActivity(context, viewIntent, null)
}

fun testOpenUrl(view: View, urlString: String) {
    view.openUrl(urlString)
}

fun View.openPhone(phone: String) {
    val viewIntent = Intent(
        Intent.ACTION_DIAL,
        Uri.parse("tel:${phone.trim()}")
    )
    ContextCompat.startActivity(context, viewIntent, null)
}

fun testOpenPhone(view: View, phone: String) {
    view.openPhone(phone)
}

fun bitmapFromDrawable(
    context: Context,
    @DrawableRes id: Int
): Bitmap? {
    // Получаем векторную картину
    val vectorDrawable = ResourcesCompat.getDrawable(
        context.resources,
        id,
        null
    )

    // Конвертируем в битмап
    vectorDrawable?.let {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)

        return bitmap
    }

    // Если картинку не нашли - возвращаем null
    Log.e("Bitmap", "Resource not found")
    return null
}

/**
 * Отключает nestedScroll во viewPager, чтобы viewPager не перехватывал жесты у bottomSheet
 */
fun ViewPager2.disableNestedScrolling() {
    (getChildAt(0) as? RecyclerView)?.apply {
        isNestedScrollingEnabled = false
        overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }
}
