package ru.aioki.myapplication.data.datasource

import com.aioki.api.client.ApiClient
import com.aioki.api.client.models.login.RegistrationRequest
import ru.aioki.myapplication.base.BaseDataSource
import javax.inject.Inject

class LoginDataSource @Inject constructor(
    private val apiClient: ApiClient,
) : BaseDataSource() {

    suspend fun login(
        email: String, password: String
    ) = getResult { apiClient.signIn(email, password) }

    fun logout() = apiClient.logout()

    fun isAuth() = apiClient.isUserAuthorized()

    suspend fun signUp(
        registrationRequest: RegistrationRequest
    ) = getResult { apiClient.signUp(registrationRequest) }
}