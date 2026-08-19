package com.example.chatai.data.remote.api

import com.example.chatai.data.remote.dto.RefreshRequest
import com.example.chatai.data.remote.dto.RefreshResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/refresh")
    suspend fun refresh(
        @Body request: RefreshRequest
    ): Response<RefreshResponse>
}