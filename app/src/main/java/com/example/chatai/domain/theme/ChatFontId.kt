package com.example.chatai.domain.theme

import androidx.compose.ui.text.TextStyle
import com.example.chatai.presentation.ui.theme.AppFonts

enum class ChatFontId(
    val label: String,
    val fontStyle: TextStyle
) {
    DEFAULT("По умолчанию", AppFonts.Lato),
    LATO("Lato", AppFonts.Lato),
    SERIF("Serif", AppFonts.Serif),
    MONOSPACE("Monospace", AppFonts.Monospace),
    MARCK_SCRIPT("Marck Script", AppFonts.MarckScriptStyle)
}