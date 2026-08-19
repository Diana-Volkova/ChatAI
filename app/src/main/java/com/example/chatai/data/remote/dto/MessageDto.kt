package com.example.chatai.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MessageDto(
    val id: Long,
    @SerializedName("chat_id")
    val chatId: Int,
    val text: String,
    val sender: String,
    val timestamp: Long
)