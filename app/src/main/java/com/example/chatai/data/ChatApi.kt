package com.example.chatai.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Unit>


    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<TokenResponse>


    @POST("chat")
    suspend fun sendMsg(
        @Body msg: MessageDto
    ): Response<MessageDto>
}