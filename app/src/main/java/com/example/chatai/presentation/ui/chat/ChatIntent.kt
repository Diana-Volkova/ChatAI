package com.example.chatai.presentation.ui.chat

sealed class ChatIntent {
    data class SendMessage(
        val chatId: Int,
        val text: String
    ) : ChatIntent()
}