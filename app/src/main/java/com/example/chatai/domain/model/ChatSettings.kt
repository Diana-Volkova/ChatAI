package com.example.chatai.domain.model

import com.example.chatai.domain.theme.ChatThemeId

data class ChatSettings(
    val chatId: Int,
    val theme: ChatThemeId
)