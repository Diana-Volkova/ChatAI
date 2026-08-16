package com.example.chatai.domain.usecase

import com.example.chatai.data.local.SessionManager
import com.example.chatai.domain.repository.AuthRepository
import com.example.chatai.domain.repository.ChatRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager,
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke() {
        repository.deleteAccount()

        sessionManager.clear()
        chatRepository.clearAll()
    }
}