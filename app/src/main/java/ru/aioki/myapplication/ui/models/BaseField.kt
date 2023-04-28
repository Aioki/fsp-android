package ru.aioki.myapplication.ui.models

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.aioki.myapplication.MainApplication
import ru.aioki.myapplication.R
import ru.aioki.myapplication.utils.LOCAL_DATE_FORMAT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

interface SetText {
    fun setText(value: String?)
}

interface Error {
    val isError: Boolean
    fun setError(errorText: String?)
}

abstract class BaseField<T>(private val required: Boolean) : Error {

    val data = MutableLiveData<T>().apply {
        observeForever {
            if (required && validationCondition(it)) {
                val valid = checkValid(it)
                errorText.value = valid.errorText.orEmpty()
            }
        }
    }
    val errorText = MediatorLiveData<String?>()
    val isErrorEnabled = errorText.map {
        !it.isNullOrEmpty()
    }.apply {
        observeForever {

        }
    }

    override val isError: Boolean
        get() = isErrorEnabled.value == true

    abstract fun checkValid(value: T): State

    override fun setError(errorText: String?) {
        this.errorText.value = errorText
    }

    open fun validationCondition(value: T): Boolean {
        return value != null
    }

    companion object {
        data class State(val isSuccess: Boolean, val errorText: String? = null) {
            companion object {
                fun success() = State(true, null)
                fun error(text: String) = State(false, text)
                fun error(@StringRes string: Int) =
                    State(false, MainApplication.res.getString(string))
            }
        }
    }
}


abstract class BaseStringField(required: Boolean) : BaseField<String>(required), SetText {

    override fun setText(value: String?) {
        value?.let {
            data.value = it
        }
    }

    override fun checkValid(value: String): Companion.State {
        return if (value.isNotEmpty()) {
            Companion.State.success()
        } else {
            Companion.State.error(R.string.required_field)
        }
    }
}

abstract class BaseNStringField(required: Boolean) : BaseField<String?>(required), SetText {

    override fun setText(value: String?) {
        value?.let {
            data.value = it
        }
    }

    override fun checkValid(value: String?): Companion.State {
        return if (value?.length != 0) {
            Companion.State.success()
        } else {
            Companion.State.error(R.string.required_field)
        }
    }
}


abstract class BaseDateField(required: Boolean) : BaseStringField(required) {

    abstract fun convertFun(long: Long?): String?

    val longDate = MutableLiveData<Long>().apply {
        observeForever {
            data.value = convertFun(it)
        }
    }
}

open class DateField(required: Boolean) : BaseDateField(required) {
    override fun convertFun(long: Long?): String? {
        TODO("Необходима функция по преобразованию из Long в строку, которая отобразиться в поле")
        // Пример:

//        return DateConverter.longToDisplay(long)
    }
}

class BirthdayField(required: Boolean) : DateField(required) {
    override fun checkValid(value: String): Companion.State {
        // Проверка на пустую строку (Пустая строка считается валидной т.к. дата рождения - опциональное поле)
        if(value.isEmpty()) return Companion.State.success()

        val sdf = SimpleDateFormat(LOCAL_DATE_FORMAT)
        sdf.isLenient = false // Отключаем для точной валидации даты

        try {
            // Парсим строку
            val time = sdf.parse(value)

            val calendar = Calendar.getInstance() // Получаем календарь
            val currentTime = calendar.time // Сохраняем текущую дату
            calendar.time = time // Записываем значение из строки в календарь

            return when {
                // Дата рождения не может быть раньше 1900 года
                calendar.get(Calendar.YEAR) < 1900 -> Companion.State.error(R.string.invalid_birth_date)
                // Дата рождения не может быть позже текущей даты
                // TODO: добавить минимальный возраст?
                calendar.time > currentTime -> Companion.State.error(R.string.invalid_birth_date)
                // Дата рождения корректна
                else -> Companion.State.success()
            }
        }
        catch (e: ParseException) {
            // Строка не распарсилась - некорректный формат строки
            return Companion.State.error(R.string.invalid_birth_date)
        }

        // Пример:
//        val newValue = longDate.value ?: return Companion.State.success()
//        val cal = Calendar.getInstance()
//        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - MINIMUM_AGE)
//        return if (newValue <= cal.timeInMillis) {
//            Companion.State.success()
//        } else {
//            Companion.State.error(R.string.IncorrectDateSelected)
//        }
    }
}

class DateTimeField(required: Boolean) : BaseDateField(required) {
    override fun convertFun(long: Long?): String? {
        TODO("Необходима функция по преобразованию из Long в строку, которая отобразиться в поле")
        // Пример:
//        return DateConverter.longToDisplayDateTime(long)
    }
}

open class StringField(required: Boolean) : BaseStringField(required)

class NEmailField(required: Boolean) : BaseNStringField(required) {
    override fun checkValid(value: String?): Companion.State {
        if (value == null) return Companion.State.success()
        return when {
            Patterns.EMAIL_ADDRESS.matcher(value).matches() -> {
                Companion.State.success()
            }
            value.isEmpty() -> {
                Companion.State.success()
            }
            else -> {
                Companion.State.error(R.string.invalid_email)
            }

        }
    }
}

class EmailField(required: Boolean) : BaseStringField(required) {
    override fun checkValid(value: String): Companion.State {
        return when {
            Patterns.EMAIL_ADDRESS.matcher(value).matches() -> {
                Companion.State.success()
            }
            else -> {
                Companion.State.error(R.string.invalid_email)
            }
        }
    }
}

/**
 * NullableString
 * */
class NStringField(required: Boolean) : BaseNStringField(required)

class PhoneNumberField(required: Boolean) : BaseStringField(required) {
    // Регулярное выражение для проверки номера телефона
    private val regexPattern =
        "^((8|\\+7)[\\- ]?)\\(?\\d{3}\\)?[\\- ]?\\d{3}[\\- ]?\\d{2}[\\- ]?(\\d{2})\$"

    override fun checkValid(value: String): Companion.State {
        return when {
            Regex(regexPattern).containsMatchIn(value) -> {
                Companion.State.success()
            }
            else -> {
                Companion.State.error(R.string.invalid_phone_number)
            }
        }
    }

    val isValid: Boolean
        get() = data.value?.let { checkValid(it).isSuccess } ?: false

    private var needValidation = false

    val clearPhone: String?
        get() = data.value?.replace("-", "")?.replace(" ", "")

    override fun validationCondition(value: String): Boolean {
        // Пропускаем валидацию первой не пустой строки
        return if (needValidation) {
            true
        } else {
            needValidation = value.isNotBlank()
            false
        }
    }
}
