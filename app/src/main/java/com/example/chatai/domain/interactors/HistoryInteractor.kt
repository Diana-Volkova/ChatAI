package com.example.chatai.domain.interactors

import com.example.chatai.domain.model.Message
import com.example.chatai.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryInteractor @Inject constructor(
    private val repo: ChatRepository
) {
    fun observeHistory(chatId: Int): Flow<List<Message>> {
        return repo.observeHistory(chatId)
    }

    suspend fun syncHistory(chatId: Int) {
        repo.syncMessages(chatId)
    }

    suspend fun clearHistory(chatId: Int) {
        repo.clearHistory(chatId)
    }
}