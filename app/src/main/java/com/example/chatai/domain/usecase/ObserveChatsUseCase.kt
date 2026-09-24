package com.example.chatai.domain.usecase

import com.example.chatai.domain.model.Chat
import com.example.chatai.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatsUseCase @Inject constructor(
    private val repository: ChatRepository,
) {
    operator fun invoke(): Flow<List<Chat>> {
        return repository.observeChats()
    }
}