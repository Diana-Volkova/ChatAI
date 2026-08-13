package com.example.chatai.presentation.ui.chat

import com.example.chatai.data.Message

sealed class ChatIntent {
    data class SendMessage(
        val chatId: Int,
        val text: String
    ) : ChatIntent()
}