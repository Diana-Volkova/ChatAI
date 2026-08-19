package com.example.chatai.data.remote.dto

data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String
)