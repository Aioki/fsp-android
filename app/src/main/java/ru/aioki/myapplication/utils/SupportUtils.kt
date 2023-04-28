package ru.aioki.myapplication.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager
import androidx.core.view.WindowCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Класс для выполнения кода с задержкой
 */
class CodeThrottle(val minInterval: Int) {

    private var lastEventTime = System.currentTimeMillis()

    fun throttle(code: () -> Unit) {
        val eventTime = System.currentTimeMillis()
        if (eventTime - lastEventTime > minInterval) {
            lastEventTime = eventTime
            code()
        }
    }
}

fun pxToDp(context: Context, value: Int): Int {
    return (value / context.resources.displayMetrics.density).roundToInt()
}

fun dpToPx(context: Context, value: Int): Int {
    return (value * context.resources.displayMetrics.density).roundToInt()
}

/**
 * Конвертирует дату в строке в другой формат
 * @param date Входная строка с датой
 * @param from Формат даты во входной строке
 * @param to Результирующий формат даты
 * @return Строка с датой в новом формате
 */
fun convertDateFormat(date: String?, from: String, to: String): String? {
    date?.let {
        val inputSDF = SimpleDateFormat(from, Locale.getDefault())
        inputSDF.isLenient = false

        val outputSDF = SimpleDateFormat(to, Locale.getDefault())
        outputSDF.isLenient = false

        try {
            inputSDF.parse(date)?.let {
                return outputSDF.format(it)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    return null
}

fun dateToLong(date: String?, format: String): Long? {
    date?.let {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.isLenient = false

        try {
            sdf.parse(date)?.let {
                return it.time
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    return null
}

fun longDateToString(date: Long?, format: String): String? {
    date?.let {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.isLenient = false

        return sdf.format(date)
    }

    return null
}

fun longDateToUTC(date: Long?): Long? {
    return date?.let {
        val timezone = TimeZone.getDefault()
        date + timezone.getOffset(date)
    }
}

object StatusBarModeManager {

    /**
     * Установка светлой темы строки состояния
     */
    fun setStatusBarLightMode(activity: Activity, light: Boolean) {
        if (Build.MANUFACTURER.uppercase() == XIAOMI_MODEL || Build.MODEL.uppercase() == XIAOMI_MODEL) {
            setMiuiStatusBarLightMode(activity, light)
        } else if (Build.MANUFACTURER.uppercase() == MEIZU_MODEL || Build.MODEL.uppercase() == MEIZU_MODEL) {
            setFlymeStatusBarLightMode(activity, light)
        }
        WindowCompat.getInsetsController(
            activity.window,
            activity.window.decorView
        ).isAppearanceLightStatusBars = light
    }

    /**
     * Установка светлой темы строки состояния на телефонах с оболочкой MIUI
     */
    private fun setMiuiStatusBarLightMode(activity: Activity, light: Boolean): Boolean {
        var result = false
        val window = activity.window
        if (window != null) {
            try {
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                val lightModeFlag = field.getInt(layoutParams)
                val extraFlagField = window.javaClass.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                extraFlagField.invoke(
                    window,
                    if (light) lightModeFlag else 0,
                    lightModeFlag
                )
                result = true
            } catch (e: Exception) {
            }
        }
        return result
    }

    /**
     * Установка светлой темы строки состояния на телефонах с оболочкой Flyme
     */
    private fun setFlymeStatusBarLightMode(activity: Activity, light: Boolean): Boolean {
        var result = false
        val window = activity.window
        if (window != null) {
            try {
                val layoutParams = window.attributes
                val lightFlag =
                    WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags =
                    WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
                lightFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = lightFlag.getInt(null)
                var value = meizuFlags.getInt(null)
                if (light) {
                    value = value or bit
                } else {
                    value = value and bit.inv()
                }
                meizuFlags.setInt(layoutParams, value)
                window.attributes = layoutParams
                result = true
            } catch (e: Exception) {
            }
        }
        return result
    }

    /**
     * Название телефонов xiaomi
     */
    const val XIAOMI_MODEL = "XIAOMI"

    /**
     * Название телефонов meizu
     */
    const val MEIZU_MODEL = "MEIZU"
}

fun toDate(date: String?, format: String): Date? {
    date?.let {
        val inputSDF = SimpleDateFormat(format, Locale.getDefault())
        inputSDF.isLenient = false

        try {
            return inputSDF.parse(it)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
    return null
}

fun fromDateToString(date: Date?, format: String): String? {
    date?.let {
        val inputSDF = SimpleDateFormat(format, Locale.getDefault())
        inputSDF.isLenient = false

        try {
            return inputSDF.format(it)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
    return null
}
