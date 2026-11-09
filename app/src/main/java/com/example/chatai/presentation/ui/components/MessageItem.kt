package com.example.chatai.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.chatai.R
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import com.example.chatai.presentation.ui.theme.chat.ChatUiTokens.Message.GRADIENT_END_ALPHA
import com.example.chatai.presentation.ui.theme.chat.ChatUiTokens.Message.SELECTION_ALPHA
import com.example.chatai.presentation.ui.theme.Dimens
import com.example.chatai.presentation.ui.utils.formatTime

@Composable
fun MessageItem(
    message: Message,
    fontFamily: FontFamily,
    selected: Boolean,
    selecting: Boolean,
    onDelete: () -> Unit,
    onSelect: () -> Unit,
    onGenerateAnotherAnswer: () -> Unit,
) {
    var menuExpanded by remember(message.id) { mutableStateOf(false) }

    var selectedAnswer by remember(
        message.id,
        message.text,
        message.alternatives,
    ) {
        mutableIntStateOf(message.alternatives.size)
    }

    val density = LocalDensity.current
    val layoutState = remember(density) {
        MessageItemLayoutState(density)
    }

    val isUser = message.sender == Sender.USER

    val bubbleColor = if (isUser) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    val contentColor = if (isUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    val timestampColor = if (isUser) {
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
    }

    val bubbleShape = remember(message.sender) {
        message.sender.toBubbleShape()
    }

    val bubbleGradient = remember(bubbleColor) {
        verticalGradient(
            colors = listOf(
                bubbleColor,
                bubbleColor.copy(alpha = GRADIENT_END_ALPHA),
            ),
        )
    }

    val answers = remember(message.text, message.alternatives) {
        listOf(message.text) + message.alternatives
    }

    val answerIndex = selectedAnswer.coerceIn(answers.indices)
    val currentAnswer = answers[answerIndex]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (selected) {
                    MaterialTheme.colorScheme.primary.copy(
                        alpha = SELECTION_ALPHA
                    )
                } else {
                    Color.Transparent
                }
            )
            .combinedClickable(
                onClick = {
                    if (selecting) onSelect()
                    else menuExpanded = true
                },
                onLongClick = onSelect,
            ),
        horizontalArrangement = if (isUser) {
            Arrangement.End
        } else {
            Arrangement.Start
        },
    ) {
        Surface(
            modifier = Modifier
                .padding(
                    horizontal = Dimens.Message.horizontalOuterPadding,
                    vertical = Dimens.Message.verticalOuterPadding,
                )
                .background(bubbleGradient, bubbleShape),
            shape = bubbleShape,
            color = Color.Transparent,
            contentColor = contentColor,
        ) {
            BoxWithConstraints {
                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .widthIn(max = maxWidth)
                        .padding(
                            horizontal = Dimens.Message.horizontalPadding,
                            vertical = Dimens.Message.verticalPadding,
                        ),
                    verticalArrangement = layoutState.arrangement,
                ) {
                    if (isUser) {
                        Text(
                            text = currentAnswer,
                            modifier = Modifier.padding(end = layoutState.textPadding),
                            onTextLayout = { layoutState.textLayout = it },
                            fontFamily = fontFamily,
                        )

                        MessageTimestamp(
                            timestamp = message.timestamp,
                            modifier = Modifier.align(Alignment.End),
                            fontFamily = fontFamily,
                            color = timestampColor,
                            onSizeChanged = { layoutState.timeSize = it },
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (answers.size > 1) {
                                IconButton(
                                    onClick = { selectedAnswer = answerIndex - 1 },
                                    enabled = answerIndex > 0,
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .size(40.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(
                                                start = 10.dp,
                                                end = 2.dp,
                                            )
                                            .size(32.dp)
                                            .background(
                                                color = contentColor.copy(alpha = 0.10f),
                                                shape = CircleShape,
                                            ),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                            contentDescription = stringResource(R.string.previous_answer),
                                            modifier = Modifier.size(20.dp),
                                            tint = contentColor.copy(alpha = 0.75f),
                                        )
                                    }
                                }
                            } else {
                                IconButton(
                                    onClick = onGenerateAnotherAnswer,
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .size(40.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(
                                                start = 10.dp,
                                                end = 2.dp,
                                            )
                                            .size(32.dp)
                                            .background(
                                                color = contentColor.copy(alpha = 0.10f),
                                                shape = CircleShape,
                                            ),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = stringResource(R.string.generate_another_answer),
                                            modifier = Modifier.size(20.dp),
                                            tint = contentColor.copy(alpha = 0.75f),
                                        )
                                    }
                                }
                            }

                            Text(
                                text = currentAnswer,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = layoutState.textPadding),
                                onTextLayout = { layoutState.textLayout = it },
                                fontFamily = fontFamily,
                            )

                            if (answers.size > 1) {
                                IconButton(
                                    onClick = { selectedAnswer = answerIndex + 1 },
                                    enabled = answerIndex < answers.lastIndex,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .size(40.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(
                                                start = 2.dp,
                                                end = 10.dp,
                                            )
                                            .size(32.dp)
                                            .background(
                                                color = contentColor.copy(alpha = 0.10f),
                                                shape = CircleShape,
                                            ),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                            contentDescription = stringResource(R.string.next_answer),
                                            modifier = Modifier.size(20.dp),
                                            tint = contentColor.copy(alpha = 0.75f),
                                        )
                                    }
                                }
                            }
                        }

                        MessageTimestamp(
                            timestamp = message.timestamp,
                            modifier = Modifier.align(Alignment.End),
                            fontFamily = fontFamily,
                            color = timestampColor,
                            onSizeChanged = { layoutState.timeSize = it },
                        )
                    }
                }

                MessageItemMenu(
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    containerWith = maxWidth,
                    message = message,
                    onDelete = onDelete,
                )
            }
        }
    }
}

@Composable
private fun MessageTimestamp(
    timestamp: Long,
    fontFamily: FontFamily,
    color: Color,
    modifier: Modifier,
    onSizeChanged: (IntSize) -> Unit,
) {
    Text(
        text = formatTime(timestamp),
        modifier = modifier
            .onSizeChanged(onSizeChanged),
        style = MaterialTheme.typography.labelSmall,
        fontFamily = fontFamily,
        color = color
    )
}

private fun Sender.toBubbleShape() = when (this) {
    Sender.USER -> RoundedCornerShape(
        topStart = Dimens.Message.bubbleCorner,
        topEnd = Dimens.Message.bubbleCorner,
        bottomStart = Dimens.Message.bubbleCorner,
        bottomEnd = Dimens.Message.smallCorner,
    )

    Sender.ASSISTANT -> RoundedCornerShape(
        topStart = Dimens.Message.bubbleCorner,
        topEnd = Dimens.Message.bubbleCorner,
        bottomStart = Dimens.Message.smallCorner,
        bottomEnd = Dimens.Message.bubbleCorner,
    )
}

private class MessageItemLayoutState(
    private val density: Density
) {
    var textLayout by mutableStateOf<TextLayoutResult?>(null)
    var containerSize by mutableStateOf(IntSize.Zero)
    var timeSize by mutableStateOf(IntSize.Zero)

    val isInline by derivedStateOf {
        textLayout != null &&
                timeSize != IntSize.Zero &&
                containerSize != IntSize.Zero &&
                textLayout!!.size.width < containerSize.width / 2
    }

    val isFreeSpace by derivedStateOf {
        textLayout != null &&
                timeSize != IntSize.Zero &&
                containerSize != IntSize.Zero &&
                textLayout!!.lineCount > 1 &&
                run {
                    val layout = textLayout!!
                    val lastX = layout.getLineRight(
                        layout.lineCount - 1
                    )
                    val freeSpace = layout.size.width - lastX

                    freeSpace > timeSize.width
                }
    }

    val arrangement: Arrangement.Vertical by derivedStateOf {
        if (isInline || isFreeSpace) {
            Arrangement.spacedBy(
                density.run {
                    -timeSize.height.toDp()
                }
            )
        } else {
            Arrangement.Top
        }
    }

    val textPadding by derivedStateOf {
        if (isInline) {
            density.run {
                timeSize.width.toDp()
            } + Dimens.Message.timestampTextPadding
        } else {
            0.dp
        }
    }
}