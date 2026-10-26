package com.example.chatai.domain.exception

class ChatException : Exception {

    val code: Int?

    constructor(code: Int) : super() {
        this.code = code
    }

    constructor(message: String) : super(message) {
        this.code = null
    }
}