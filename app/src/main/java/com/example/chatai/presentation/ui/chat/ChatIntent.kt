package com.example.chatai.presentation.ui.chat

sealed class ChatIntent {

    data class LoadHistory(
        val chatId: Int
    ) : ChatIntent()
    data class SendMessage(
        val chatId: Int,
        val text: String
    ) : ChatIntent()

    data class DeleteMessages(
        val chatId: Int,
        val messageIds: List<Long>
    ) : ChatIntent()

    data class ClearHistory(
        val chatId: Int
    ) : ChatIntent()
}