package com.example.chatai.domain.interactors

import com.example.chatai.domain.model.Message
import com.example.chatai.domain.repository.ChatRepository
import javax.inject.Inject

class HistoryInteractor @Inject constructor(
    private val repo: ChatRepository
) {
    suspend fun loadHistory(chatId: Int): List<Message> {
        return repo.loadHistory(chatId)
    }

    suspend fun syncHistory(chatId: Int) {
        repo.syncMessages(chatId)
    }

    suspend fun clearHistory(chatId: Int) {
        repo.clearHistory(chatId)
    }
}