package com.aioki.api.client

import android.content.Context
import androidx.lifecycle.LiveData
import com.aioki.api.client.api.AccountApi
import com.aioki.api.client.api.EventApi
import com.aioki.api.client.api.FeedbackApi
import com.aioki.api.client.interceptors.RefreshTokenInjector
import com.aioki.api.client.interceptors.TokenInjector
import com.aioki.api.client.interceptors.UserAgentInterceptor
import com.aioki.api.client.models.login.AccountResponse
import com.aioki.api.client.models.login.RegistrationRequest
import com.aioki.api.client.models.login.SubmitCodeRequest
import com.squareup.moshi.Moshi
import kotlinx.coroutines.sync.Mutex
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class ApiClient(
    private val context: Context,
    moshi: Moshi,
    apiUrl: String,
    versionName: String,
) {

    private val credentialRepository = AuthCredentialRepository(context)
    private val refreshCredentialMutex = Mutex()

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .addInterceptor(UserAgentInterceptor("okhttp", versionName))
        .addInterceptor(
            TokenInjector {
                credentialRepository.accessToken
            }
        )
        .addInterceptor(RefreshTokenInjector(credentialRepository::refreshToken))
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(apiUrl)
            .addConverterFactory(
                MoshiConverterFactory.create(moshi)
            )
            .build()
    }

//    private fun refreshCredentialsSync() {
//        runBlocking {
//            refreshCredentials()
//        }
//    }

    suspend fun signIn(phone: String, code: String): retrofit2.Response<AccountResponse> {
        val res = accountApi.login(SubmitCodeRequest(phone, code))
        if (res.isSuccessful) {
//            val refreshToken =
//                res.headers()["set-cookie"]?.split(';')?.find { s -> s.startsWith("refreshToken") }
//                    ?.dropWhile { c -> c != '=' }?.drop(1)
            val accessToken = res.body()?.token
            credentialRepository.accessToken = accessToken
//            credentialRepository.refreshToken = refreshToken
        }
        return res
    }

    suspend fun signUp(registrationRequest: RegistrationRequest): retrofit2.Response<AccountResponse> {
        val res = accountApi.register(registrationRequest)
        if (res.isSuccessful) {
//            val refreshToken =
//                res.headers()["set-cookie"]?.split(';')?.find { s -> s.startsWith("refreshToken") }
//                    ?.dropWhile { c -> c != '=' }?.drop(1)
            val accessToken = res.body()?.token
            credentialRepository.accessToken = accessToken
//            credentialRepository.refreshToken = refreshToken
        }
        return res
    }

    fun logout() {
//        val res = accountApi.logout()
//        if (res.isSuccessful) {
//        }
//        return res
        credentialRepository.clear()

    }


    val accountApi: AccountApi by lazy { retrofit.create() }
    val feedbackApi: FeedbackApi by lazy { retrofit.create() }
    val eventApi: EventApi by lazy { retrofit.create() }


//    private suspend fun refreshCredentials() {
//        val oldRefreshToken = credentialRepository.refreshToken ?: return
//        refreshCredentialMutex.withLock {
//            // Токен может обновиться пока ожидается лок
//            if (oldRefreshToken != credentialRepository.refreshToken) return
//
//            val res = accountApi.refreshToken()
//
//            if (res.isSuccessful) {
//                Log.e(TAG, "refreshCredentials: success")
//
//                val refreshToken =
//                    res.headers()["set-cookie"]?.split(';')
//                        ?.find { s -> s.startsWith("refreshToken") }
//                        ?.dropWhile { c -> c != '=' }?.drop(1)
//                val accessToken = res.body()?.token
//                credentialRepository.accessToken = accessToken
//                credentialRepository.refreshToken = refreshToken
//            } else {
//                Log.e(TAG, "refreshCredentials: failure")
//
//                if (res.code() == HttpURLConnection.HTTP_UNAUTHORIZED || res.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
//                    credentialRepository.clear()
//                    context.sendBroadcast(Intent(BROADCAST_ACTION_UNAUTHORIZED))
//                } else {
//                    throw HttpException(res)
//                }
//            }
//        }
//    }

    //TODO to refactor
    fun isUserAuthorized(): Boolean {
        return credentialRepository.accessToken != null
    }

    fun isUserAuthorizedLiveData(): LiveData<Boolean> {
        return credentialRepository.isAuthorizedLiveData
    }

    companion object {
        private const val TAG = "ApiClient"

        const val BROADCAST_ACTION_UNAUTHORIZED =
            "ru.aioki.myapplication.UNAUTHORIZED_BROADCAST"
    }

}