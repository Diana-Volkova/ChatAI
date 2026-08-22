package com.example.chatai.domain.repository

import com.example.chatai.domain.model.ChatSettings
import com.example.chatai.domain.theme.ChatThemeId
import kotlinx.coroutines.flow.Flow

interface ChatSettingsRepository {
    fun observe(chatId: Int): Flow<ChatSettings>
    suspend fun setTheme(chatId: Int,theme: ChatThemeId)
}