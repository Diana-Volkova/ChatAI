package com.example.chatai.domain.usecase

import com.example.chatai.data.local.SessionManager
import com.example.chatai.domain.error.AuthException
import com.example.chatai.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke() {
        val refreshToken = sessionManager.refreshToken()
            ?: throw AuthException(401)

        repository.logout(refreshToken)
        sessionManager.clear()
    }
}