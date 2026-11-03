package com.example.chatai.domain.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.example.chatai.presentation.ui.theme.AppFonts

enum class ChatFontId(
    val label: String,
    val fontStyle: TextStyle
) {
    DEFAULT("По умолчанию", AppFonts.LatoStyle),
    LATO("Lato", AppFonts.LatoStyle),
    SERIF("Serif", TextStyle(fontFamily = FontFamily.Serif)),
    MONOSPACE("Monospace", TextStyle(fontFamily = FontFamily.Monospace)),
    MARCK_SCRIPT("Marck Script", AppFonts.MarckScriptStyle)
}