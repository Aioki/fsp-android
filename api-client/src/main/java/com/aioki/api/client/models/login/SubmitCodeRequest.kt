package com.aioki.api.client.models.login

data class SubmitCodeRequest(
    val phoneNumber: String,
    val password: String
)
