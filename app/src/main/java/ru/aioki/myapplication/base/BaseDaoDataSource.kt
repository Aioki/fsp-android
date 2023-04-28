package ru.aioki.myapplication.base

import android.database.sqlite.SQLiteException

abstract class BaseDaoDataSource {

    protected suspend fun <T> getResultAsync(call: suspend () -> T): T {
        try {
            val response = call()
            return response
        } catch (e: SQLiteException) {
            e.printStackTrace()
            throw SQLiteException(e.message ?: e.toString())
        }
    }

    protected fun <T> getResult(call: () -> T): T {
        try {
            val response = call()
            return response
        } catch (e: SQLiteException) {
            e.printStackTrace()
            throw SQLiteException(e.message ?: e.toString())
        }
    }
}