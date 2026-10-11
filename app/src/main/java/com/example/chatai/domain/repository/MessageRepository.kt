package com.example.chatai.domain.repository

import com.example.chatai.domain.model.ChatDetails
import com.example.chatai.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun observeHistory(chatId: Int): Flow<ChatDetails>
    suspend fun sendMessage(
        chatId: Int,
        message: Message
    ): Message

    suspend fun generateAlternative(
        chatId: Int,
        message: Message
    ): Message

    suspend fun syncChat(chatId: Int)

    suspend fun clearHistory(chatId: Int)

    suspend fun deleteMessagesList(
        chatId: Int,
        messageIds: List<Long>
    )
    suspend fun clearAll()
}