package com.example.chatai.domain.repository

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String,
    )
    suspend fun register(
        email: String,
        password: String,
    )
    suspend fun logout(refreshToken: String)
    suspend fun deleteAccount()
}