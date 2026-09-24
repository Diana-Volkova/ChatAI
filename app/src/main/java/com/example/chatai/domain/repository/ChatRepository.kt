package com.example.chatai.domain.repository

import com.example.chatai.domain.model.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeChats(): Flow<List<Chat>>

    suspend fun syncChats()
}