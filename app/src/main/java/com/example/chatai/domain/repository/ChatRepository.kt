package com.example.chatai.domain.repository

import com.example.chatai.data.remote.dto.ChatDto
import com.example.chatai.domain.model.Message

interface ChatRepository {
    suspend fun loadHistory(chatId: Int): List<Message>
    suspend fun loadChats(): List<ChatDto>
    suspend fun sendMessage(
        chatId: Int,
        message: Message
    ): Message

    suspend fun clearHistory(chatId: Int)
    suspend fun clearAll()
}