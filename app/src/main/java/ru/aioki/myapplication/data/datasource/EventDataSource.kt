package ru.aioki.myapplication.data.datasource

import com.aioki.api.client.api.EventApi
import ru.aioki.myapplication.base.BaseDataSource
import javax.inject.Inject

class EventDataSource @Inject constructor(
    private val eventApi: EventApi,
) : BaseDataSource() {

    suspend fun fetchAll() = getResult { eventApi.fetchAll() }
}