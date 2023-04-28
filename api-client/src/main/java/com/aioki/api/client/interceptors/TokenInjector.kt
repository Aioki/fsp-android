package com.aioki.api.client.interceptors

import com.aioki.api.client.annotations.InjectToken
import okhttp3.Interceptor
import okhttp3.Response

internal class TokenInjector(
    private val tokenProvider: TokenProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val injectRefreshToken = chain.request().isAnnotationPresent(InjectToken::class)
        val token = tokenProvider.get()
        if (!injectRefreshToken || token == null) {
            return chain.proceed(chain.request())
        }

        return chain.proceed(
            chain.request()
                .newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        )
    }

    fun interface TokenProvider {
        fun get(): String?
    }
}