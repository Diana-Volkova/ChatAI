package com.example.chatai.data.remote.dto

data class MessageDto(
    val text: String,
    val sender: String,
    val timestamp: Long
)