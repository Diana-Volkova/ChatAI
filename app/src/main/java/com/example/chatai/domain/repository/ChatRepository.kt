package com.example.chatai.domain.repository

import com.example.chatai.data.remote.dto.ChatDto
import com.example.chatai.data.remote.dto.MessageDto
import com.example.chatai.domain.model.Chat
import com.example.chatai.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeChats(): Flow<List<Chat>>

    suspend fun syncChats()
}