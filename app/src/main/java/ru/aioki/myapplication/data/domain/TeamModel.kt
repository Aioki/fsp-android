package ru.aioki.myapplication.data.domain

import ru.aioki.myapplication.utils.Decimal

data class TeamModel(
    val name: String,
    val rating: Decimal?,
    val members: List<ProfileModel>,
)
