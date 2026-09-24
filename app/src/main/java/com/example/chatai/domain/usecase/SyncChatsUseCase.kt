package com.example.chatai.domain.usecase

import com.example.chatai.domain.repository.ChatRepository
import javax.inject.Inject

class SyncChatsUseCase @Inject constructor(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke() {
        repository.syncChats()
    }
}