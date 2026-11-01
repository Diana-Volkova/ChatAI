package com.example.chatai.domain.theme

import androidx.compose.ui.text.font.FontFamily
import com.example.chatai.presentation.ui.theme.AppFonts

enum class ChatFontId(
    val label: String,
    val fontFamily: FontFamily
) {
    DEFAULT("По умолчанию", AppFonts.LatoRegular),
    LATO("Lato", AppFonts.LatoRegular),
    SERIF("Serif", FontFamily.Serif),
    MONOSPACE("Monospace", FontFamily.Monospace),
    INDIE_FLOWER("Indie Flower", AppFonts.IndieFlower)
}