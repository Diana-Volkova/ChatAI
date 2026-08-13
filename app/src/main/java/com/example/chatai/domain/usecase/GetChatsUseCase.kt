package com.example.chatai.domain.usecase

import com.example.chatai.domain.repository.ChatRepository

class GetChatsUseCase(
    private val repository: ChatRepository,
) {
    //suspend operator fun invoke() =
        //repository.loadHistory(chatId)
}