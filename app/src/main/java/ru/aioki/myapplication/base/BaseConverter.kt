package ru.aioki.myapplication.base

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

abstract class BaseConverter<T> {

    protected val moshi: Moshi = mMoshi

    abstract fun toString(data: T?): String?

    abstract fun fromString(strData: String?): T?

    companion object {
        private val mMoshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}