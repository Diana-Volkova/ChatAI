package com.example.chatai.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MessageDto(
    val id: Long,
    val text: String,
    val sender: String,
    val timestamp: Long,

    @SerializedName("user_message_id")
    val userMessageId: Long? = null,

    val alternatives: List<String> = emptyList()
)