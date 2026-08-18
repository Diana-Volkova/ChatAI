package com.example.chatai.domain.usecase

import com.example.chatai.domain.repository.ChatRepository
import javax.inject.Inject

class SyncMessagesUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    suspend operator fun invoke(chatId: Int) {
        repo.syncMessages(chatId)
    }
}