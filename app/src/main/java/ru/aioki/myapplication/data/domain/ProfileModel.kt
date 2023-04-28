package ru.aioki.myapplication.data.domain

import com.aioki.api.client.models.FileApiModel
import com.aioki.api.client.models.profile.ProfileApiModel
import ru.aioki.myapplication.utils.Decimal

data class ProfileModel(
    val lastName: String?,
    val firstName: String?,
    val middleName: String?,
    val phone: String,
    val aboutMe: String?,
    val gender: Gender?, //male or female
    val avatar: FileApiModel?,
    val rating: Decimal?,
    val role: Role,
) {
    companion object {
        enum class Gender(val value: String) {
            MALE("male"),
            FEMALE("female");

            companion object {
                fun fromString(value: String?) =
                    Gender.values().firstOrNull { g -> g.value == value }
            }
        }
    }
}

fun ProfileApiModel.toDomain(): ProfileModel {
    TODO()
}
