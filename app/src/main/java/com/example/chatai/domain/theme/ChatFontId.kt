package com.example.chatai.domain.theme

import androidx.compose.ui.text.font.FontFamily
import com.example.chatai.presentation.ui.theme.chat.AppFonts

enum class ChatFontId(
    val label: String,
    val font: FontFamily
) {
    DEFAULT("По умолчанию", FontFamily.SansSerif),
    LATO("Lato", AppFonts.Lato),
    JOST("Jost", AppFonts.Jost),
    SERIF("Serif", FontFamily.Serif),
    MONOSPACE("Monospace", FontFamily.Monospace),
    MARCK_SCRIPT("Marck Script", AppFonts.MarckScript),
    HACHI_MARU_POP_REGULAR("Hachi Maru Pop", AppFonts.HachiMaruPopRegular)
}