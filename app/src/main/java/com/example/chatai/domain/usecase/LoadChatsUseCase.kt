package com.example.chatai.domain.usecase

import com.example.chatai.data.remote.dto.ChatDto
import com.example.chatai.domain.repository.ChatRepository
import javax.inject.Inject

class LoadChatsUseCase @Inject constructor(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke(): List<ChatDto> {
        return repository.loadChats()
    }
}