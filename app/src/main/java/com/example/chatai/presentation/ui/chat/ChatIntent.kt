package com.example.chatai.presentation.ui.chat

import com.example.chatai.domain.theme.ChatThemeId

sealed class ChatIntent {

    data class ObserveSettings(val chatId: Int) : ChatIntent()
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

    data class SetTheme(
        val chatId: Int,
        val chatThemeId: ChatThemeId
    ) : ChatIntent()
}