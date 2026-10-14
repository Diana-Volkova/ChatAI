package com.example.chatai.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import com.example.chatai.presentation.ui.utils.formatTime

@Composable
fun MessageItem(
    message: Message,
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
    val colors = MaterialTheme.colorScheme

    val bubbleColor = if (isUser) colors.primary else colors.surfaceVariant
    val contentColor = if (isUser) colors.onPrimary else colors.onSurfaceVariant

    val bubbleShape = remember(message.sender) {
        message.sender.toBubbleShape()
    }

    val bubbleGradient = remember(bubbleColor) {
        verticalGradient(
            colors = listOf(
                bubbleColor,
                bubbleColor.copy(alpha = 0.85f),
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
                    colors.primary.copy(alpha = 0.2f)
                } else {
                    Color.Transparent
                },
            )
            .combinedClickable(
                onClick = {
                    if (selecting) onSelect() else menuExpanded = true
                },
                onLongClick = onSelect,
            )
            .onSizeChanged { layoutState.containerSize = it },
        horizontalArrangement = if (isUser) {
            Arrangement.End
        } else {
            Arrangement.Start
        },
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .background(bubbleGradient, bubbleShape),
            shape = bubbleShape,
            color = Color.Transparent,
            contentColor = contentColor,
        ) {
            BoxWithConstraints {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 8.dp,
                    ),
                    verticalArrangement = layoutState.arrangement,
                ) {
                    Text(
                        text = currentAnswer,
                        modifier = Modifier.padding(
                            end = layoutState.textPadding,
                        ),
                        onTextLayout = { layoutState.textLayout = it },
                    )

                    MessageTimestamp(
                        timestamp = message.timestamp,
                        modifier = Modifier.align(Alignment.End),
                        onSizeChanged = { layoutState.timeSize = it },
                    )

                    if (!isUser) {
                        AssistantMessageActions(
                            selectedAnswer = answerIndex,
                            answerCount = answers.size,
                            onGenerateAnotherAnswer = onGenerateAnotherAnswer,
                            onSelectAnswer = { selectedAnswer = it },
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
    modifier: Modifier = Modifier,
    onSizeChanged: (IntSize) -> Unit,
) {
    Text(
        text = formatTime(timestamp),
        modifier = modifier
            .alpha(0.7f)
            .onSizeChanged(onSizeChanged),
        style = MaterialTheme.typography.labelSmall,
    )
}

@Composable
private fun AssistantMessageActions(
    selectedAnswer: Int,
    answerCount: Int,
    onGenerateAnotherAnswer: () -> Unit,
    onSelectAnswer: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (answerCount == 1) {
            IconButton(
                onClick = onGenerateAnotherAnswer,
                modifier = Modifier.size(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Сгенерировать другой ответ",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                        .copy(alpha = 0.7f),
                )
            }
        }

        Spacer(Modifier.weight(1f))

        if (answerCount > 1) {
            AnswerSwitcher(
                selectedAnswer = selectedAnswer,
                answerCount = answerCount,
                onSelectAnswer = onSelectAnswer,
            )
        }
    }
}

@Composable
private fun AnswerSwitcher(
    selectedAnswer: Int,
    answerCount: Int,
    onSelectAnswer: (Int) -> Unit,
) {
    val iconTint = MaterialTheme.colorScheme.onSurfaceVariant
        .copy(alpha = 0.7f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        IconButton(
            onClick = { onSelectAnswer(selectedAnswer - 1) },
            enabled = selectedAnswer > 0,
            modifier = Modifier.size(24.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Предыдущий ответ",
                tint = iconTint,
            )
        }

        Text(
            text = "${selectedAnswer + 1}/$answerCount",
            style = MaterialTheme.typography.labelSmall,
            color = iconTint,
        )

        IconButton(
            onClick = { onSelectAnswer(selectedAnswer + 1) },
            enabled = selectedAnswer < answerCount - 1,
            modifier = Modifier.size(24.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Следующий ответ",
                tint = iconTint,
            )
        }
    }
}

private fun Sender.toBubbleShape() = when (this) {
    Sender.USER -> RoundedCornerShape(
        topStart = 18.dp,
        topEnd = 18.dp,
        bottomStart = 18.dp,
        bottomEnd = 4.dp,
    )

    Sender.ASSISTANT -> RoundedCornerShape(
        topStart = 18.dp,
        topEnd = 18.dp,
        bottomStart = 4.dp,
        bottomEnd = 18.dp,
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
            } + 4.dp
        } else {
            0.dp
        }
    }
}