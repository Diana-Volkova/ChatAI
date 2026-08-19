package com.example.chatai.domain.error

class ChatException : Exception {

    val code: Int?

    constructor(code: Int) : super() {
        this.code = code
    }

    constructor(message: String) : super(message) {
        this.code = null
    }

    fun message(): String =
        when (code) {
            401 -> "Сессия уже недействительна"
            500 -> "Ошибка сервера"
            null -> super.message ?: "Произошла ошибка"
            else -> "Произошла ошибка: $code"
        }
}