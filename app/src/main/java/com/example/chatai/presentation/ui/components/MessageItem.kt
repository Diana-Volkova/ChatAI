package com.example.chatai.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.chatai.domain.model.Message
import com.example.chatai.domain.model.Sender
import formatTime

@Composable
fun MessageItem(
    message: Message,
    selected: Boolean,
    selecting: Boolean,
    onDelete: () -> Unit,
    onSelect: () -> Unit
) {
    val density = LocalDensity.current
    val layoutState = remember(density) { MessageItemLayoutState(density) }
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    when (selecting) {
                        true -> onSelect()
                        false -> menuExpanded = true
                    }
                },
                onLongClick = onSelect,
            )

            .fillMaxWidth()
            .background(if (selected) MaterialTheme.colorScheme.primary.copy(0.2f) else Color.Transparent)
            .onSizeChanged {
                layoutState.containerSize = it
            },
        horizontalArrangement = when (message.sender) {
            Sender.USER -> Arrangement.End
            Sender.ASSISTANT -> Arrangement.Start
        },
    ) {
        Surface(
            color = when (message.sender) {
                Sender.USER -> MaterialTheme.colorScheme.primary
                Sender.ASSISTANT -> MaterialTheme.colorScheme.surfaceVariant
            },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier

                .padding(
                    horizontal = 8.dp,
                    vertical = 4.dp,
                ),
        ) {
            BoxWithConstraints {
                Column(
                    verticalArrangement = layoutState.arrangement,
                    modifier = Modifier
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp,
                        ),
                ) {
                    Text(
                        text = message.text,
                        onTextLayout = {
                            layoutState.textLayout = it
                        },
                        modifier = Modifier.padding(
                            end = layoutState.textPadding,
                        ),
                    )

                    Text(
                        text = formatTime(message.timestamp),
                        modifier = Modifier
                            .onSizeChanged {
                                layoutState.timeSize = it
                            }
                            .align(Alignment.End)
                            .alpha(0.7f),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

                MessageItemMenu(
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    containerWith = maxWidth,
                    message = message,
                    onDelete = onDelete
                )
            }
        }
    }
}

private class MessageItemLayoutState(private val density: Density) {
    var textLayout by mutableStateOf<TextLayoutResult?>(null)
    var containerSize by mutableStateOf(IntSize.Zero)
    var timeSize by mutableStateOf(IntSize.Zero)


    val isInline by derivedStateOf {
        textLayout != null &&
                timeSize != IntSize.Zero &&
                containerSize != IntSize.Zero &&
                textLayout!!.size.width < containerSize.width / 2
    }

    val isFreeSpace by
    derivedStateOf {
        textLayout != null &&
                timeSize != IntSize.Zero &&
                containerSize != IntSize.Zero &&
                textLayout!!.lineCount > 1 &&
                run {
                    val layout = textLayout!!
                    val lastX = layout.getLineRight(layout.lineCount - 1)
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