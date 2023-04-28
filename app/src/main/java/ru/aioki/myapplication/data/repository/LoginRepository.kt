package ru.aioki.myapplication.data.repository

import com.aioki.api.client.models.login.RegistrationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.aioki.myapplication.data.datasource.LoginDataSource
import ru.aioki.myapplication.utils.Resource
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginDataSource: LoginDataSource,
) {

    suspend fun login(email: String, password: String): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            loginDataSource.login(email, password).map { }
        }
    }

    fun logout() {
        loginDataSource.logout()
    }

    fun isAuth(): Boolean {
        return loginDataSource.isAuth()
    }

    suspend fun signUp(
        registrationRequest: RegistrationRequest
    ): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            loginDataSource.signUp(registrationRequest).map { }
        }
    }


}