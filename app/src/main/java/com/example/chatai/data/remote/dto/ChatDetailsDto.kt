package com.example.chatai.data.remote.dto

data class ChatDetailsDto(
    val id: Int,
    val title: String,
    val model: String,
    val lastMessageAt: Long?,
    val messages: List<MessageDto>
)