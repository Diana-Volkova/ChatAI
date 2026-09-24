package com.example.chatai.domain.repository

import com.example.chatai.data.remote.dto.MessageDto
import com.example.chatai.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun observeHistory(chatId: Int): Flow<List<Message>>
    suspend fun sendMessage(
        chatId: Int,
        message: Message
    ): Message

    suspend fun getRemoteMessages(
        chatId: Int
    ): List<MessageDto>

    suspend fun syncMessages(chatId: Int)

    suspend fun clearHistory(chatId: Int)

    suspend fun deleteMessagesList(
        chatId: Int,
        messageIds: List<Long>
    )
    suspend fun clearAll()
}