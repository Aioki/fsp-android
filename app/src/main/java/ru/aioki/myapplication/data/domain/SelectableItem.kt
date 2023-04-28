package ru.aioki.myapplication.data.domain

interface SelectableItem {
    val id: Int

    override fun equals(other: Any?): Boolean
}