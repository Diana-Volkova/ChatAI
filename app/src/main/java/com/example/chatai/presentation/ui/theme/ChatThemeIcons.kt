package com.example.chatai.presentation.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChatThemeBackIcon = ImageVector.Builder(
    name = "ChatThemeBack",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color.Black),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(15f, 18f)
        lineTo(9f, 12f)
        lineTo(15f, 6f)
    }
}.build()

val ChatThemeDefaultIcon = ImageVector.Builder(
    name = "ChatThemeDefault",
    defaultWidth = 24.dp,
    defaultHeight = 24f.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = null,
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.8f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        // Центральная точка
        moveTo(12f, 10f)
        lineTo(12f, 14f)

        // Левая точка
        moveTo(5f, 10f)
        lineTo(5f, 14f)

        // Правая точка
        moveTo(19f, 10f)
        lineTo(19f, 14f)

        // Верхняя точка
        moveTo(8.5f, 6.5f)
        lineTo(8.5f, 9f)

        // Нижняя точка
        moveTo(15.5f, 15f)
        lineTo(15.5f, 17.5f)
    }
}.build()

val ChatThemeMidnightIcon = ImageVector.Builder(
    name = "ChatThemeMidnight",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = null,
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.8f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        // Луна
        moveTo(15.5f, 4f)
        lineTo(14.2f, 4.2f)
        lineTo(12.8f, 4.8f)
        lineTo(11.5f, 5.7f)
        lineTo(10.5f, 6.8f)
        lineTo(9.8f, 8.2f)
        lineTo(9.4f, 9.7f)
        lineTo(9.4f, 11.2f)
        lineTo(9.8f, 12.7f)
        lineTo(10.5f, 14f)
        lineTo(11.5f, 15.2f)
        lineTo(12.8f, 16f)
        lineTo(14.2f, 16.6f)
        lineTo(15.7f, 16.8f)
        lineTo(17.2f, 16.6f)
        lineTo(18.6f, 16f)
        lineTo(19.8f, 15.1f)

        // Внутренняя часть луны
        moveTo(15.5f, 4f)
        lineTo(16.2f, 5.2f)
        lineTo(16.6f, 6.5f)
        lineTo(16.6f, 7.9f)
        lineTo(16.2f, 9.3f)
        lineTo(15.5f, 10.5f)
        lineTo(14.5f, 11.5f)
        lineTo(13.3f, 12.2f)
        lineTo(12f, 12.5f)
        lineTo(10.7f, 12.5f)

        // Звезда
        moveTo(18f, 5f)
        lineTo(18f, 7f)

        moveTo(17f, 6f)
        lineTo(19f, 6f)

        // Маленькая звезда
        moveTo(6f, 8f)
        lineTo(6f, 9.5f)

        moveTo(5.25f, 8.75f)
        lineTo(6.75f, 8.75f)

        // Ещё одна маленькая звезда
        moveTo(19f, 11f)
        lineTo(19f, 12f)

        moveTo(18.5f, 11.5f)
        lineTo(19.5f, 11.5f)
    }
}.build()

val ChatThemeForestIcon = ImageVector.Builder(
    name = "ChatThemeForest",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color.Black),
        strokeLineWidth = 1.8f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        // ветка
        moveTo(6f, 18f)
        curveTo(10f, 15f, 13f, 11f, 16f, 6f)

        // левый лист
        moveTo(10f, 14f)
        curveTo(7f, 14f, 5f, 12f, 5f, 9f)
        curveTo(8f, 9f, 11f, 11f, 10f, 14f)

        // правый лист
        moveTo(13f, 11f)
        curveTo(13f, 8f, 15f, 6f, 18f, 6f)
        curveTo(18f, 9f, 16f, 11f, 13f, 11f)

        // верхний лист
        moveTo(15f, 8f)
        curveTo(14f, 5f, 16f, 3f, 19f, 3f)
        curveTo(20f, 6f, 18f, 8f, 15f, 8f)
    }
}.build()
val ChatThemeSunsetIcon = ImageVector.Builder(
    name = "ChatThemeSunset",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = null,
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.8f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        // Полукруг солнца
        moveTo(7f, 13f)
        lineTo(7.5f, 11f)
        lineTo(8.5f, 9.2f)
        lineTo(10f, 8f)
        lineTo(12f, 7.5f)
        lineTo(14f, 8f)
        lineTo(15.5f, 9.2f)
        lineTo(16.5f, 11f)
        lineTo(17f, 13f)

        // Горизонт
        moveTo(4f, 14f)
        lineTo(20f, 14f)

        // Луч сверху
        moveTo(12f, 4.5f)
        lineTo(12f, 3f)

        // Левый луч
        moveTo(7.5f, 6.5f)
        lineTo(6.3f, 5.3f)

        // Правый луч
        moveTo(16.5f, 6.5f)
        lineTo(17.7f, 5.3f)

        // Волны
        moveTo(5f, 17f)
        lineTo(7f, 16f)
        lineTo(9f, 17f)
        lineTo(11f, 16f)

        moveTo(13f, 17f)
        lineTo(15f, 16f)
        lineTo(17f, 17f)
        lineTo(19f, 16f)
    }
}.build()