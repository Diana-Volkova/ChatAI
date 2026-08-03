package com.example.chatai.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/refresh")
    suspend fun refresh(
        @Body request: RefreshRequest
    ): Response<RefreshResponse>
}