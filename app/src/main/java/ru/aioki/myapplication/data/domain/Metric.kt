package ru.aioki.myapplication.data.domain

data class Metric(
    val id: Int,
    val name: String,
    val value: Int? = null
) {
    fun setValue(value: Int) = copy(value = value)
}
