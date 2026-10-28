package com.example.chatai.presentation.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.chatai.presentation.ui.theme.ChatUiTokens
import com.example.chatai.presentation.ui.theme.Dimens
import androidx.compose.ui.unit.IntOffset
import kotlin.math.pow

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    val tokens = ChatUiTokens.Loading
    val transition = rememberInfiniteTransition(label = "floating")

    val floatOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = -tokens.floatOffset,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = tokens.animationDurationMillis,
                easing = FastOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "floatOffset",
    )

    val shadowScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = tokens.shadowMinScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = tokens.animationDurationMillis,
                easing = FastOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "shadowScale",
    )

    val shadowAlpha by transition.animateFloat(
        initialValue = tokens.shadowMaxAlpha,
        targetValue = tokens.shadowMinAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = tokens.animationDurationMillis,
                easing = FastOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "shadowAlpha",
    )

    val progressSize = Dimens.Progress.size

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .offset(y = tokens.shadowOffset.dp)
                .size(
                    width = progressSize * tokens.shadowWidthFactor,
                    height = progressSize * tokens.shadowHeightFactor,
                )
                .graphicsLayer {
                    scaleX = shadowScale
                    scaleY = shadowScale
                },
        ) {
            drawOval(
                brush = Brush.radialGradient(
                    colorStops = createShadowColorStops(
                        alpha = shadowAlpha
                    ),
                    center = center,
                    radius = size.width / 2f,
                ),
            )
        }

        CircularProgressIndicator(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = 0,
                        y = floatOffset.dp.roundToPx(),
                    )
                }
                .size(progressSize),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = Dimens.Progress.strokeWidth,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
        )
    }
}

private fun createShadowColorStops(
    alpha: Float,
): Array<Pair<Float, Color>> {
    val steps = ChatUiTokens.Loading.shadowGradientSteps
    val power = ChatUiTokens.Loading.shadowGradientPower

    return Array(steps + 1) { index ->
        val fraction = index.toFloat() / steps
        val opacity = alpha * (1f - fraction).pow(power)

        fraction to Color.Black.copy(alpha = opacity)
    }.also {
        it[it.lastIndex] = 1f to Color.Transparent
    }
}