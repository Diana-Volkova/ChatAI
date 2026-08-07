package com.example.chatai.data

data class Message(
    val id: Long = 0,
    val chatId: Int,
    val text: String,
    val sender: Sender,
    val timestamp: Long
)
enum class Sender {
    USER,
    ASSISTANT
}