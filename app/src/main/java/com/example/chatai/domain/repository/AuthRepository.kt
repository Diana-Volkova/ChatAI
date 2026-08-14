package com.example.chatai.domain.repository

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String,
    )
}