package com.example.chatai.domain.usecase

import com.example.chatai.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ) {
        repository.register(email, password)
    }
}