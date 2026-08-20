package com.example.chatai.data.remote.api

import com.example.chatai.data.remote.dto.ChatDto
import com.example.chatai.data.remote.dto.LoginRequest
import com.example.chatai.data.remote.dto.MessageDto
import com.example.chatai.data.remote.dto.RefreshRequest
import com.example.chatai.data.remote.dto.RegisterRequest
import com.example.chatai.data.remote.dto.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
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

    @DELETE("auth/account")
    suspend fun deleteAccount(): Response<Unit>

    @POST("chats/{chat_id}/messages")
    suspend fun sendMsg(
        @Path("chat_id") chatId: Int,
        @Body msg: MessageDto
    ): Response<MessageDto>

    @GET("chats")
    suspend fun getChats(): Response<List<ChatDto>>

    @GET("chats/{chat_id}/messages")
    suspend fun getMessages(
        @Path("chat_id") chatId: Int
    ): Response<List<MessageDto>>

    @DELETE("chats/{chat_id}/messages")
    suspend fun deleteMessages(
        @Path("chat_id") chatId: Int
    ): Response<Unit>

    @HTTP(
        method = "DELETE",
        path = "chats/{chat_id}/messages/bulk",
        hasBody = true
    )
    suspend fun deleteMessagesList(
        @Path("chat_id") chatId: Int,
        @Body messageIds: List<Long>
    ): Response<Unit>
}