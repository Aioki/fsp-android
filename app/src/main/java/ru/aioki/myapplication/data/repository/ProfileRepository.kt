package ru.aioki.myapplication.data.repository

import ru.aioki.myapplication.data.datasource.ProfileDataSource
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileDataSource: ProfileDataSource,
) {



}