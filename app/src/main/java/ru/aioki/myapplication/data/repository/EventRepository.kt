package ru.aioki.myapplication.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.aioki.myapplication.data.datasource.EventDataSource
import ru.aioki.myapplication.data.domain.Event
import ru.aioki.myapplication.data.domain.toDomain
import ru.aioki.myapplication.utils.Resource
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventDataSource: EventDataSource,
) {

    suspend fun fetchAll(): Resource<List<Event>> {
        return withContext(Dispatchers.IO) {
            eventDataSource.fetchAll().map { it?.map { i -> i.toDomain() } }
        }
    }

}