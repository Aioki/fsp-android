package ru.aioki.myapplication.data.datasource

import ru.aioki.myapplication.base.BaseDataSource
import ru.aioki.myapplication.utils.Resource
import java.io.File
import javax.inject.Inject

class ProfileDataSource @Inject constructor(

) : BaseDataSource() {

    suspend fun uploadPhoto(file: File): Resource<Unit> {
        TODO()
    }

    suspend fun editProfile(): Resource<Unit> {
        TODO()
    }
}