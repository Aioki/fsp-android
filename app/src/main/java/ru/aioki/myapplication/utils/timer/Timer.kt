package ru.aioki.myapplication.utils.timer

import androidx.lifecycle.MutableLiveData
import ru.aioki.myapplication.utils.MS_IN_SEC

interface Timer {

    /**
     * Информация о таймере
     */
    val timer : MutableLiveData<TimerInfo>

    /**
     * Запустить таймер
     * @param durationMs время работы в секундах
     */
    fun timerStart(durationMs : Long, interval: Long = MS_IN_SEC)

    /**
     * Остановить таймер
     */
    fun timerStop()

}

/**
 * Информация о таймере
 */
sealed class TimerInfo {

    /**
     * Таймер работает
     * @param millisUntilFinished текущее время таймера в секундах
     */
    class Started(val millisUntilFinished : Long) : TimerInfo()

    /**
     * Таймер завершился
     */
    object Finished : TimerInfo()
}
