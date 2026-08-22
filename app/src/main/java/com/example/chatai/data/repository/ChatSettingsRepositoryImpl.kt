package com.example.chatai.data.repository

import com.example.chatai.data.local.ChatSettingsDao
import com.example.chatai.data.local.ChatSettingsEntity
import com.example.chatai.data.mappers.toDomain
import com.example.chatai.domain.model.ChatSettings
import com.example.chatai.domain.repository.ChatSettingsRepository
import com.example.chatai.domain.theme.ChatThemeId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatSettingsRepositoryImpl(
    private val dao: ChatSettingsDao
) : ChatSettingsRepository {

    override fun observe(chatId: Int): Flow<ChatSettings> {
        return dao.observe(chatId)
            .map { entity ->
                entity?.toDomain()
                    ?: ChatSettings(
                        chatId = chatId,
                        theme = ChatThemeId.DEFAULT
                    )
            }
    }

    override suspend fun setTheme(
        chatId: Int,
        theme: ChatThemeId
    ) {
        dao.upsert(
            ChatSettingsEntity(
                chatId = chatId,
                theme = theme.name
            )
        )
    }
}