package ru.aioki.myapplication.utils

import android.app.Activity
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.aioki.myapplication.R

fun TextView.makeLink(clickablePart: String, clickListener: View.OnClickListener) {
    val spannableString = SpannableString(this.text)

    val startIndexOfLink = this.text.indexOf(clickablePart)
    val endIndexOfLink = this.text.indexOf(clickablePart) + clickablePart.length

    // TODO: выбрасывать исключение?
    if(startIndexOfLink == -1) return

    val clickableSpan = object : ClickableSpan() {
        override fun updateDrawState(textPaint: TextPaint) {
            textPaint.color = ResourcesCompat.getColor(resources, R.color.red1, null)
            textPaint.isUnderlineText = true
        }

        override fun onClick(view: View) {
            Selection.setSelection((view as TextView).text as Spannable, 0)
            view.invalidate()
            clickListener.onClick(view)
        }
    }

    spannableString.setSpan(
        clickableSpan, startIndexOfLink, endIndexOfLink,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

/**
 * Navigate с защитой от множественного срабатывания (например, при двойном клике на кнопку открытия диалога)
 */
fun NavController.navigateSafely(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.let {
        navigate(direction, it.navOptions?.copy {
            launchSingleTop = true
        })
    }
}

/**
 * Создает копию NavOptions и применяет к ней NavOptionsBuilder
 */
fun NavOptions.copy(optionsBuilder: NavOptionsBuilder.() -> Unit): NavOptions {
    return navOptions {
        launchSingleTop = this@copy.shouldLaunchSingleTop()
        restoreState = this@copy.shouldRestoreState()

        val route = this@copy.popUpToRoute
        if (route == null) {
            popUpTo(this@copy.popUpToId) {
                inclusive = this@copy.isPopUpToInclusive()
                saveState = this@copy.shouldPopUpToSaveState()
            }
        } else {
            popUpTo(route) {
                inclusive = this@copy.isPopUpToInclusive()
                saveState = this@copy.shouldPopUpToSaveState()
            }
        }

        anim {
            enter = this@copy.enterAnim
            exit = this@copy.exitAnim
            popEnter = this@copy.popEnterAnim
            popExit = this@copy.popExitAnim
        }

        optionsBuilder()
    }
}

// Смена темы статус бара
fun Activity.setLightStatusBars(b: Boolean) {
    StatusBarModeManager.setStatusBarLightMode(this, b)
}

/**
 * Отключение множественного выбора одного и того же пункта в навигационном меню
 */
fun BottomNavigationView.disableItemsReselected() {
    this.setOnItemReselectedListener { }
}


/**
 * Single time event LiveData
 *
 * Обсервер подписывается на события лайвдаты, и после того как он получит изменения в первый раз,
 * отпишется от данного события
 * */
/*
 * Функция обсервера сделана не в виде лямбды, т.к. если лямбда не обращается к полям в
 * данном классе, то она будет скомпилирована в виде синглтона для оптимизации, что приведёт
 * к исключению:
 * "IllegalArgumentException: Cannot add the same observer with different lifecycles",
 * т.е. когда будут идти 2 подряд одинаковых фрагмента, 2 жиз. цикла будут ссылаться на один
 * обсервер.
 *
 * Поэтому вместо лямбды каждый раз при инициализации создаём новый экземпляр класса Observer
 * и переопределяем функцию onChanged
 *
 * P.S. https://stackoverflow.com/a/54939860
 * */
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}
