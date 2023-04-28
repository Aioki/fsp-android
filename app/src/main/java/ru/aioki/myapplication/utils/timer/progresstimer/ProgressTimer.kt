package ru.aioki.myapplication.utils.timer.progresstimer

import androidx.lifecycle.MutableLiveData

interface ProgressTimer {

    /**
     * Текущий прогресс (от 0 до 100)
     */
    val progress : MutableLiveData<Int>

    /**
     * Запустить таймер
     * @param durationMS время работы в миллисекундах
     */
    fun timerStart(durationMS : Long)

    /**
     * Остановить таймер
     */
    fun timerStop()

    /**
     * Поставить таймер на паузу
     */
    fun timerPause()

    /**
     * Возобновить таймер после паузы
     */
    fun timerResume()

    /**
     * Запущен ли таймер
     */
    fun isTimerStarted() : Boolean

    /**
     * На паузе ли таймер
     */
    fun isTimerPaused() : Boolean

}