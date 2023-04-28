package com.aioki.api.client.api

import com.aioki.api.client.annotations.InjectAccessToken
import com.aioki.api.client.models.BaseResponse
import com.aioki.api.client.models.feedback.FeedbackRequest
import com.aioki.api.client.models.feedback.FeedbackResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedbackApi {

    /**
     * Получить данные для отзыва к заказу
     *
     * @param orderId идентификатор заказа
     * */
    @InjectAccessToken
    @GET("order/feedback/")
    suspend fun getFeedbackQuestions(
        @Query("orderId") orderId: Int,
    ): Response<FeedbackResponse>

    /**
     * Отправить отзыв о заказе
     * */
    suspend fun sendFeedback(
        @Body params: FeedbackRequest,
    ): Response<BaseResponse>
}