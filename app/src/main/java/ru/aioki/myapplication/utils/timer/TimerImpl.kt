package ru.aioki.myapplication.utils.timer

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import ru.aioki.myapplication.utils.MS_IN_SEC

class TimerImpl : Timer {

    // Таймер
    private var resendSMSTimer : CountDownTimer? = null

    override val timer: MutableLiveData<TimerInfo> = MutableLiveData<TimerInfo>()

    override fun timerStart(durationMs: Long, interval: Long) {
        timerStop()
        resendSMSTimer = object : CountDownTimer(durationMs * MS_IN_SEC, interval) {
            override fun onTick(millisUntilFinished: Long) {
                timer.value = TimerInfo.Started(millisUntilFinished)
            }
            override fun onFinish() {
                timer.value = TimerInfo.Finished
            }
        }.start()
    }

    override fun timerStop() {
        resendSMSTimer?.cancel()
        timer.value = TimerInfo.Finished
    }
}