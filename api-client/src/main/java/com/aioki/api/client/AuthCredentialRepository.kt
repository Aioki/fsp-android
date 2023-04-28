package com.aioki.api.client

import android.content.Context
import androidx.lifecycle.map
import com.aioki.api.client.temp.stringLiveData

internal class AuthCredentialRepository(context: Context) {

    private val prefs = context.getSharedPreferences(CREDENTIAL_STORE_NAME, Context.MODE_PRIVATE)

    var accessToken: String?
        get() = prefs.getString(ACCESS_TOKEN_KEY, null)
        set(value) {
            prefs.edit()
                .putString(ACCESS_TOKEN_KEY, value)
                .apply()
        }

    var refreshToken: String?
        get() = prefs.getString(REFRESH_TOKEN_KEY, null)
        set(value) {
            prefs.edit()
                .putString(REFRESH_TOKEN_KEY, value)
                .apply()
        }

    val isAuthorizedLiveData = prefs.stringLiveData(ACCESS_TOKEN_KEY, null).map {
        it != null
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val CREDENTIAL_STORE_NAME = "com.aioki.api.client.auth.cred"
        private const val REFRESH_TOKEN_KEY = "refresh-token"
        private const val ACCESS_TOKEN_KEY = "access-token"
    }
}