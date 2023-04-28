package ru.aioki.myapplication

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        res = resources

    }


    companion object {
        private const val TAG = "MainApplication"
        lateinit var res: Resources
    }
}