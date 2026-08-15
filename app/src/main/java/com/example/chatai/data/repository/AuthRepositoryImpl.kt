package com.example.chatai.data.repository

import com.example.chatai.data.local.SessionManager
import com.example.chatai.data.remote.api.ChatApi
import com.example.chatai.data.remote.dto.LoginRequest
import com.example.chatai.data.remote.dto.RegisterRequest
import com.example.chatai.domain.error.AuthException
import com.example.chatai.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: ChatApi,
    private val sessionManager: SessionManager
) : AuthRepository {
    override suspend fun login(email: String, password: String) {
        val response = api.login(
            LoginRequest(
                email = email,
                password = password,
            )
        )

        if (!response.isSuccessful) {
            throw AuthException(response.code())
        }

        val body = response.body()
            ?: throw IllegalStateException("Empty response")

        sessionManager.saveTokens(
            accessToken = body.access_token,
            refreshToken = body.refresh_token,
        )
    }

    override suspend fun register(
        email: String,
        password: String,
    ) {
        val response = api.register(
            RegisterRequest(
                email = email,
                password = password,
            )
        )

        if (!response.isSuccessful) {
            throw AuthException(response.code())
        }
    }
}