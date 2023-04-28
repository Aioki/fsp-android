package com.aioki.api.client.models.login

import com.aioki.api.client.models.BaseResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Возвращает тип подтверждения авторизации - по коду из смс либо по звонку на указанный номер телефона
 *
 * @param type enum: ["sms","call"]
 * */
@JsonClass(generateAdapter = true)
data class RegistrationRequest(
    @Json(name = PHONE_NUMBER) val phoneNumber: String,
    @Json(name = PASSWORD) val password: String,
    @Json(name = FIRST_NAME) val firstName: String,
    @Json(name = LAST_NAME) val lastName: String,
    @Json(name = MIDDLE_NAME) val middleName: String? = null,

    ) : BaseResponse() {

    companion object {
        const val PHONE_NUMBER = "phoneNumber"
        const val PASSWORD = "password"
        const val FIRST_NAME = "firstname"
        const val MIDDLE_NAME = "middleName"
        const val LAST_NAME = "lastName"
    }

}