package ru.aioki.myapplication.data.domain

enum class Role(val text: String) {
    MEMBERS(""),
    ADMIN(""),
    ROOT(""),
    PARTNER(""),
    REPRESENTATIVE("");

    companion object {
        fun fromData(text: String) = Role.values().firstOrNull { it.text == text }
    }
}