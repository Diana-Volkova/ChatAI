package com.example.chatai.domain.model

data class Chat(
    val id: Int,
    val title: String,
    val model: String,
    val lastMessageAt: Long?
)