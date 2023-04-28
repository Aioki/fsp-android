package ru.aioki.myapplication.ui.loginStage.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aioki.api.client.models.login.RegistrationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.aioki.myapplication.data.repository.LoginRepository
import ru.aioki.myapplication.ui.models.NStringField
import ru.aioki.myapplication.ui.models.PhoneNumberField
import ru.aioki.myapplication.ui.models.StringField
import ru.aioki.myapplication.utils.Resource
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
) : ViewModel() {

    val phone = PhoneNumberField(required = true).apply {
//        data.value = "9999999999"
    }
    val password = StringField(required = true).apply {
//        data.value = "password"
    }

    val submitPassword = object : StringField(required = true) {

        override fun checkValid(value: String): Companion.State {
            val defaultState = super.checkValid(value)
            return when {
                value != password.data.value -> {
                    Companion.State.error("Пароли не совпдадают")
                }
                !defaultState.isSuccess -> {
                    defaultState
                }
                else -> {
                    Companion.State.success()
                }
            }
        }
    }

    val firstName = StringField(required = true)
    val lastName = StringField(required = true)
    val middleName = NStringField(required = false)


    val statusStep1 = MutableSharedFlow<Resource<Unit>>(

    )
    val statusStep2 = MutableSharedFlow<Resource<Unit>>(

    )

    fun checkStep1() {
        viewModelScope.launch {
            val phoneText = phone.clearPhone
            val passText = password.data.value
            if (phoneText == null || passText == null) {
                statusStep1.emit(Resource.error("Пустые поля"))
                return@launch
            }
            if (phone.isError || password.isError) {
                statusStep1.emit(Resource.error("Введите корректные данные"))
                return@launch
            }
            statusStep1.emit(Resource.success())
        }

    }

    fun register() {
        viewModelScope.launch {
            val phoneText = phone.clearPhone!!
            val passText = password.data.value!!
            val firstNameText = firstName.data.value
            val lastNameText = lastName.data.value
            val middleNameText = middleName.data.value

            if (firstNameText.isNullOrBlank() || lastNameText.isNullOrBlank()) {
                statusStep2.emit(Resource.error("Пустые поля"))
                return@launch
            }


            statusStep2.emit(Resource.loading())
//            status.value = Resource.error("TEST")
            statusStep2.emit(
                loginRepository.signUp(
                    RegistrationRequest(
                        phoneNumber = phoneText,
                        password = passText,
                        firstName = firstNameText,
                        middleName = middleNameText,
                        lastName = lastNameText
                    )
                )
            )
        }
    }

}