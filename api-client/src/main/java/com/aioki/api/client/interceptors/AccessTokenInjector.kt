package com.aioki.api.client.interceptors

import com.aioki.api.client.annotations.InjectAccessToken
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.closeQuietly

internal class AccessTokenInjector(
    private val accessTokenProvider: AccessTokenProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!chain.request().isAnnotationPresent(InjectAccessToken::class)) {
            return chain.proceed(chain.request())
        }

        val accessToken = accessTokenProvider.get(false)
        if (accessToken != null) {
            val response = performRequest(chain, accessToken)
            if (response.code != 401) {
                return response
            }
            response.closeQuietly()
        }

        return performRequest(chain, accessTokenProvider.get(true))

    }

    private fun performRequest(chain: Interceptor.Chain, accessToken: String?): Response {
        val requestBuilder = chain.request().newBuilder()
        if (accessToken != null) {
            requestBuilder.header("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(requestBuilder.build())
    }

    fun interface AccessTokenProvider {
        fun get(refresh: Boolean): String?
    }
}