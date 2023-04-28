package com.aioki.api.client.interceptors

import com.aioki.api.client.annotations.InjectRefreshToken
import okhttp3.Interceptor
import okhttp3.Response

internal class RefreshTokenInjector(
    private val refreshTokenProvider: RefreshTokenProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val injectRefreshToken = chain.request().isAnnotationPresent(InjectRefreshToken::class)
        val refreshToken = refreshTokenProvider.get()
        if (!injectRefreshToken || refreshToken == null) {
            return chain.proceed(chain.request())
        }

        return chain.proceed(
            chain.request()
                .newBuilder()
                .header("Cookie", "refreshToken=$refreshToken")
                .build()
        )
    }

    fun interface RefreshTokenProvider {
        fun get(): String?
    }
}