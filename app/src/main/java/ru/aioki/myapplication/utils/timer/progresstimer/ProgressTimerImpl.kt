package ru.aioki.myapplication.utils.timer.progresstimer

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData

class ProgressTimerImpl : ProgressTimer {

    /*---------------------------------------Поля-------------------------------------------------*/
    private var timer : CountDownTimer? = null  // Таймер
    override val progress: MutableLiveData<Int> = MutableLiveData(MIN_PROGRESS) // Прогресс
    private var startTimeMS : Long = 0  // Начальное время таймера в мс
    private var leftTimeMS : Long = 0   // Осталось времени в мс
    private var isPause : Boolean = false   // Пауза
    private var isStarted : Boolean = false // Таймер стартовал (учитывает паузу)

    /*---------------------------------------Свойства---------------------------------------------*/
    // Вычисляет прогресс от оставшегося времени
    private fun calculateProgress() : Int {
        return (100 - (leftTimeMS.toDouble() / startTimeMS.toDouble()) * 100).toInt()
    }

    // Таймер стартовал (флаг)
    override fun isTimerStarted() : Boolean = isStarted

    // Таймер на паузе (флаг)
    override fun isTimerPaused() : Boolean = isStarted && isPause

    /*---------------------------------------Методы-----------------------------------------------*/

    override fun timerStart(durationMS: Long) {
        timerStop()
        resetProgress()
        isStarted = true
        isPause = false
        startTimeMS = durationMS
        leftTimeMS = startTimeMS
        timer = createTimer()
        timer?.start()
    }

    override fun timerStop() {
        timer?.cancel()
        timerReset()
    }

    override fun timerPause() {
        if (isTimerStarted()) {
            isPause = true
            timer?.cancel()
        }
    }

    override fun timerResume() {
        if (isTimerPaused()) {
            isPause = false
            timer = createTimer()
            timer?.start()
        }
    }

    // Сброс параметров таймера
    private fun timerReset() {
        timer = null
        isStarted = false
        isPause = false
        startTimeMS = 0
        leftTimeMS = 0
    }

    // Сброс прогресса
    private fun resetProgress() {
        progress.value = MIN_PROGRESS
    }

    // Функция создающая новый таймер
    private fun createTimer() : CountDownTimer {
        return object : CountDownTimer(leftTimeMS, TICK_INTERVAL_MS) {
            override fun onTick(millisUntilFinished: Long) {
                leftTimeMS = millisUntilFinished

                // Вычисляем прогресс
                progress.value = calculateProgress()

                // Условие, что 100 прогресса нет, но таймер больше не тикнет
                if (leftTimeMS <= TICK_INTERVAL_MS * 2) {
                    progress.value = MAX_PROGRESS
                }
            }
            override fun onFinish() {
                if (!isPause) {
                    timerStop()
                }
            }
        }
    }

    companion object {
        const val TICK_INTERVAL_MS : Long = 40
        const val MAX_PROGRESS : Int = 100
        const val MIN_PROGRESS : Int = 0
    }
}