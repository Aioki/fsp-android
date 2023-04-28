package ru.aioki.myapplication.ui.mainStage

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.aioki.myapplication.data.domain.Event
import ru.aioki.myapplication.data.repository.EventRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val eventRepository: EventRepository,
) : ViewModel() {

    val events = liveData {
        emit(eventRepository.fetchAll())
    }

    val newEvents = MediatorLiveData<List<Event>>().apply {
        addSource(events) {
            value = it.data
        }
    }

    fun setDate(startDate: LocalDate, endDate: LocalDate?) {
        val list = events.value?.data ?: return
        newEvents.value =
            list.filter { i ->
                val a = LocalDate.parse(
                    i.startDate,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz")
                )
                startDate <= a && (endDate == null || endDate >= a)
            }
    }

}