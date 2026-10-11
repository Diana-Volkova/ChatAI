package com.example.chatai.domain.usecase

import com.example.chatai.domain.model.Message
import com.example.chatai.domain.repository.MessageRepository
import javax.inject.Inject

class GenerateAlternativeUseCase @Inject constructor(
    private val repo: MessageRepository
) {
    suspend operator fun invoke(
        chatId: Int,
        message: Message
    ): Message {
        return repo.generateAlternative(
            chatId = chatId,
            message = message
        )
    }
}