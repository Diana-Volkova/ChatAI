package com.example.chatai.presentation.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.chatai.R

object AppFonts {

    val MarckScriptRegular = FontFamily(
        Font(R.font.marck_script_regular)
    )
    val MarckScriptStyle = TextStyle(
        fontFamily = MarckScriptRegular,
        fontSize = 20.sp,
        lineHeight = 30.sp
    )
    val LatoLight = FontFamily(
        Font(R.font.lato_light)
    )

    val LatoItalic = FontFamily(
        Font(R.font.lato_italic)
    )

    val LatoRegular = FontFamily(
        Font(R.font.lato_regular)
    )

    val LatoStyle = TextStyle(
        fontFamily = LatoRegular,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.5.sp
    )
}