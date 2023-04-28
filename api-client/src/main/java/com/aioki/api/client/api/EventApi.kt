package com.aioki.api.client.api

import com.aioki.api.client.annotations.InjectToken
import com.aioki.api.client.models.EventApiModel
import retrofit2.Response
import retrofit2.http.GET

interface EventApi {

    @InjectToken
    @GET("event")
    suspend fun fetchAll(): Response<List<EventApiModel>>

}