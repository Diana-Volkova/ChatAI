package com.example.chatai.presentation.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.chatai.R

object AppFonts {

    val MarckScript = FontFamily(
        Font(R.font.marck_script_regular, FontWeight.Normal, FontStyle.Normal)
    )
    val Lato = FontFamily(
        Font(R.font.lato_light, FontWeight.Light, FontStyle.Normal),
        Font(R.font.lato_regular, FontWeight.Normal, FontStyle.Normal),
        Font(R.font.lato_italic, FontWeight.Normal, FontStyle.Italic)
    )
    val HachiMaruPopRegular = FontFamily(
        Font(R.font.hachi_maru_pop_regular, FontWeight.Normal, FontStyle.Normal)
    )
}