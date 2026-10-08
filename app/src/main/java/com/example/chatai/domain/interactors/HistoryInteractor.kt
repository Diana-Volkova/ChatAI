package com.example.chatai.domain.interactors

import com.example.chatai.domain.model.ChatDetails
import com.example.chatai.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryInteractor @Inject constructor(
    private val repo: MessageRepository
) {
    fun observeHistory(chatId: Int): Flow<ChatDetails> {
        return repo.observeHistory(chatId)
    }

    suspend fun syncHistory(chatId: Int) {
        repo.syncChat(chatId)
    }

    suspend fun clearHistory(chatId: Int) {
        repo.clearHistory(chatId)
    }
}