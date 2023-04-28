package ru.aioki.myapplication.ui.loginStage.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.aioki.myapplication.data.repository.LoginRepository
import ru.aioki.myapplication.ui.models.PhoneNumberField
import ru.aioki.myapplication.ui.models.StringField
import ru.aioki.myapplication.utils.Resource
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
) : ViewModel() {

    val phone = PhoneNumberField(required = true).apply {
//        data.value = "9999999999"
    }
    val password = StringField(required = true).apply {
//        data.value = "password"
    }

    val status = MutableLiveData<Resource<Unit>>()

    fun login() {
        viewModelScope.launch {
            val phoneText = phone.clearPhone
            val passText = password.data.value
            if (phoneText == null || passText == null) {
                status.value = Resource.error("Пустые поля")
                return@launch
            }
            if (phone.isError || password.isError) {
                status.value = Resource.error("Введите корректные данные")
                return@launch
            }
            status.value = Resource.loading()
            status.value = loginRepository.login(phoneText, passText)
        }
    }

}