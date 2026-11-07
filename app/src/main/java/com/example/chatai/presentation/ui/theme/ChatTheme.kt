package com.example.chatai.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.chatai.domain.theme.ChatThemeId


val DefaultChatColorScheme = lightColorScheme(
    primary = Color(0xff65558f),
    onPrimary = Color.White,
    secondary = Color(0xff625b71),
    onSecondary = Color.White,
    tertiary = Color(0xff7e5260),
    error = Color(0xffba1a1a),

    primaryContainer = Color(0xffe9ddff),
    onPrimaryContainer = Color(0xff4d3d75),
    secondaryContainer = Color(0xffe8def8),
    onSecondaryContainer = Color(0xff4a4458),
    tertiaryContainer = Color(0xffffd9e3),
    onTertiaryContainer = Color(0xff633b48),
    errorContainer = Color(0xffffdad6),
    onErrorContainer = Color(0xff93000a),

    surfaceDim = Color(0xffded8e0),
    surface = Color(0xfdfdf7ff),
    surfaceContainer = Color(0xf8f2ecf4),
    surfaceBright = Color(0xfffdf7ff),
    inverseSurface = Color(0xfd322f35),
    inverseOnSurface = Color(0xfff5eff7),
    surfaceContainerLow = Color(0xfff8f2fa),
    surfaceContainerLowest = Color(0xffffffff),

    inversePrimary = Color(0xffcfbdfe),
    scrim =  Color(0xff000000),
    outline =  Color(0xff7a757f),
    outlineVariant =  Color(0xffc5c8ba),
    onSurfaceVariant =  Color(0xff49454e),
    onSurface =  Color(0xff1d1b20),
)

val MidnightChatColorScheme = darkColorScheme(
    primary = Color(0xFFB8A0FF),
    onPrimary = Color(0xFF24134F),

    background = Color(0xFF101014),
    onBackground = Color(0xFFE6E1E9),

    surface = Color(0xFF18181D),
    onSurface = Color(0xFFE6E1E9)
)

val ForestChatColorScheme = lightColorScheme(
    primary = Color(0xff4c662b),
    onPrimary = Color.White,
    secondary = Color(0xff586249),
    onSecondary = Color.White,
    tertiary = Color(0xff386663),
    error = Color(0xffba1a1a),

    primaryContainer = Color(0xffcdeda3),
    onPrimaryContainer = Color(0xff354e16),
    secondaryContainer = Color(0xffdce7c8),
    onSecondaryContainer = Color(0xff404a33),
    tertiaryContainer = Color(0xffbcece7),
    onTertiaryContainer = Color(0xff1f4e4b),
    errorContainer = Color(0xffffdad6),
    onErrorContainer = Color(0xff93000a),

    surfaceDim = Color(0xffdadbd0),
    surface = Color(0xfdf9faef),
    surfaceContainer = Color(0xf8eeefe3),
    surfaceBright = Color(0xfff9faef),
    inverseSurface = Color(0xff2f312a),
    inverseOnSurface = Color(0xfff1f2e6),
    surfaceContainerLow = Color(0xfff3f4e9),
    surfaceContainerLowest = Color(0xffffffff),

    inversePrimary = Color(0xffb1d18a),
    scrim =  Color(0xff000000),
    outline =  Color(0xff75796c),
    outlineVariant =  Color(0xffc5c8ba),
    onSurfaceVariant =  Color(0xff44483d),
    onSurface =  Color(0xff1a1c16),
)

val SunsetChatColorScheme = lightColorScheme(
    primary = Color(0xff8f4b3a),
    onPrimary = Color.White,
    secondary = Color(0xff77574f),
    onSecondary = Color.White,
    tertiary = Color(0xff6d5d2e),
    error = Color(0xffba1a1a),

    primaryContainer = Color(0xffffdad2),
    onPrimaryContainer = Color(0xff723425),
    secondaryContainer = Color(0xffffdad2),
    onSecondaryContainer = Color(0xff5d4038),
    tertiaryContainer = Color(0xfff7e1a6),
    onTertiaryContainer = Color(0xff544519),
    errorContainer = Color(0xffffdad6),
    onErrorContainer = Color(0xff93000a),

    surfaceDim = Color(0xffe8d6d2),
    surface = Color(0xfdfff8f6),
    surfaceContainer = Color(0xf8fceae6),
    surfaceBright = Color(0xfffff8f6),
    inverseSurface = Color(0xfd392e2b),
    inverseOnSurface = Color(0xffffede9),
    surfaceContainerLow = Color(0xfffff1ed),
    surfaceContainerLowest = Color(0xffffffff),

    inversePrimary = Color(0xffffb4a2),
    scrim =  Color(0xff000000),
    outline =  Color(0xff85736f),
    outlineVariant =  Color(0xffd8c2bd),
    onSurfaceVariant =  Color(0xff534340),
    onSurface =  Color(0xff231917),
)

@Composable
fun ChatTheme(
    theme: ChatThemeId,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        ChatThemeId.DEFAULT -> DefaultChatColorScheme
        ChatThemeId.MIDNIGHT -> MidnightChatColorScheme
        ChatThemeId.FOREST -> ForestChatColorScheme
        ChatThemeId.SUNSET -> SunsetChatColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}