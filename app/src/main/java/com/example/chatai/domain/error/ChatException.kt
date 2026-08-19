package com.example.chatai.domain.error

class ChatException(
    val code: Int
) : Exception() {
    fun message(): String =
        when (code) {
            401 -> "Сессия уже недействительна"
            500 -> "Ошибка сервера"
            else -> "Произошла ошибка:$code"
        }
}