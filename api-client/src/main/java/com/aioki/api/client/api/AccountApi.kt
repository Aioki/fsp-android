package com.aioki.api.client.api

import com.aioki.api.client.annotations.InjectToken
import com.aioki.api.client.models.BaseResponse
import com.aioki.api.client.models.login.AccountResponse
import com.aioki.api.client.models.login.RegistrationRequest
import com.aioki.api.client.models.login.SubmitCodeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApi {


    /**
     * Выйти из личного кабинета
     * */
    @InjectToken
    @POST("logout/")
    suspend fun logout(): Response<BaseResponse>

    @POST("auth/login")
    suspend fun login(
        @Body body: SubmitCodeRequest
    ): Response<AccountResponse>

    @POST("auth/register")
    suspend fun register(
        @Body body: RegistrationRequest
    ): Response<AccountResponse>
}