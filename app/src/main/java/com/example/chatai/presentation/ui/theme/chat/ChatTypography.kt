package com.example.chatai.presentation.ui.theme.chat

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

data class ChatTypography(
    val searchPlaceholder: TextStyle,
    val searchResult: TextStyle,
    val searchTimestamp: TextStyle
)

fun chatTypography(fontFamily: FontFamily) = ChatTypography(
    searchPlaceholder = TextStyle(
        fontFamily = fontFamily,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    searchResult = TextStyle(
        fontFamily = fontFamily,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    searchTimestamp = TextStyle(
        fontFamily = fontFamily,
        fontSize = 11.sp,
        lineHeight = 16.sp
    )
)