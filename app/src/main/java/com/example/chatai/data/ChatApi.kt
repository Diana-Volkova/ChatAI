package com.example.chatai.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Unit>


    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<TokenResponse>

    @POST("auth/logout")
    suspend fun logout(
        @Body request: RefreshRequest
    ): Response<Unit>
    @POST("chats/{chat_id}/messages")
    suspend fun sendMsg(
        @Path("chat_id") chatId: Int,
        @Body msg: MessageDto
    ): Response<MessageDto>

    @GET("chats")
    suspend fun getChats(): Response<List<ChatDto>>
}