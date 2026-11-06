package com.example.chatai.presentation.ui.chat

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import com.example.chatai.R
import com.example.chatai.domain.model.Message
import com.example.chatai.presentation.ui.chat.mapper.ChatListMapper
import com.example.chatai.presentation.ui.chat.model.ChatListItem
import com.example.chatai.presentation.ui.components.MessageItem
import com.example.chatai.presentation.ui.theme.ChatUiTokens.Input.DISABLED_CONTENT_ALPHA
import com.example.chatai.presentation.ui.theme.ChatUiTokens.Input.PLACEHOLDER_ALPHA
import com.example.chatai.presentation.ui.theme.Dimens
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.platform.LocalLocale
import com.example.chatai.presentation.ui.theme.ChatUiTokens.Message.DATE_SEPARATOR_ALPHA

@Composable
fun MessagesScreen(
    messages: List<Message>,
    paddingValues: PaddingValues,
    listState: LazyListState,
    fontFamily: FontFamily,
    onSendMessage: (String) -> Unit,
    selectedMessages: SnapshotStateSet<Message>,
    onDeleteMessages: (List<Long>) -> Unit,
    onGenerateAnotherAnswer: (Message) -> Unit,
    searchQuery: String,
    modifier: Modifier = Modifier,
) {
    val layoutDirection = LocalLayoutDirection.current

    Column(
        modifier = modifier
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = paddingValues.calculateStartPadding(layoutDirection),
                end = paddingValues.calculateEndPadding(layoutDirection),
            )
    ) {
        Messages(
            messages = messages,
            listState = listState,
            fontFamily = fontFamily,
            modifier = Modifier.weight(1f),
            selectedMessages = selectedMessages,
            onDeleteMessages = onDeleteMessages,
            onGenerateAnotherAnswer = onGenerateAnotherAnswer
        )

        MessageInput(
            fontFamily = fontFamily,
            onSendMessage = onSendMessage,
        )
    }
}


@Composable
fun Messages(
    messages: List<Message>,
    listState: LazyListState,
    fontFamily: FontFamily,
    selectedMessages: SnapshotStateSet<Message>,
    onDeleteMessages: (List<Long>) -> Unit,
    onGenerateAnotherAnswer: (Message) -> Unit,
    modifier: Modifier = Modifier
) {
    val chatItems = remember(messages) {
        ChatListMapper.map(messages)
    }

    LaunchedEffect(chatItems.size) {
        if (chatItems.isNotEmpty()) {
            listState.animateScrollToItem(chatItems.lastIndex)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
    ) {
        items(
            items = chatItems,
            key = { item ->
                when (item) {
                    is ChatListItem.MessageItem ->
                        "message_${item.message.id}"

                    is ChatListItem.DateSeparator ->
                        "date_${item.date}"
                }
            }
        ) { item ->

            when (item) {
                is ChatListItem.DateSeparator -> {
                    DateSeparator(item.date)
                }

                is ChatListItem.MessageItem -> {
                    val message = item.message

                    MessageItem(
                        message = message,
                        fontFamily = fontFamily,
                        selected = message in selectedMessages,
                        selecting = selectedMessages.isNotEmpty(),
                        onDelete = {
                            message.serverId?.let { serverId ->
                                onDeleteMessages(listOf(serverId))
                            }
                        },
                        onSelect = {
                            if (message in selectedMessages) {
                                selectedMessages.remove(message)
                            } else {
                                selectedMessages.add(message)
                            }
                        },
                        onGenerateAnotherAnswer = {
                            onGenerateAnotherAnswer(message)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MessageInput(
    fontFamily: FontFamily,
    onSendMessage: (String) -> Unit,
) {
    val fieldState = remember { TextFieldState() }

    val canSend = fieldState.text.isNotBlank()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .imePadding()
            .padding(
                horizontal = Dimens.Spacing.medium,
                vertical = Dimens.Spacing.small
            ),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing.small),
        verticalAlignment = Alignment.Bottom,
    ) {
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(Dimens.Shape.round),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = Dimens.Elevation.low,
        ) {
            BasicTextField(
                state = fieldState,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = fontFamily,
                ),
                lineLimits = TextFieldLineLimits.MultiLine(
                    minHeightInLines = 1,
                    maxHeightInLines = 5,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimens.Spacing.large,
                        vertical = Dimens.Spacing.medium,
                    ),
                decorator = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (fieldState.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.message),
                                style = MaterialTheme.typography.bodyLarge,
                                fontFamily = fontFamily,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                    .copy(alpha = PLACEHOLDER_ALPHA),
                            )
                        }

                        innerTextField()
                    }
                },
            )
        }

        val buttonScale by animateFloatAsState(
            targetValue = if (canSend) 1f else 0.9f,
            label = "sendButtonScale",
        )

        FilledIconButton(
            onClick = {
                val message = fieldState.text.toString()

                if (message.isNotBlank()) {
                    onSendMessage(message)
                    fieldState.clearText()
                }
            },
            enabled = canSend,
            modifier = Modifier
                .size(Dimens.Input.buttonSize)
                .graphicsLayer {
                    scaleX = buttonScale
                    scaleY = buttonScale
                },
            shape = CircleShape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    .copy(alpha = DISABLED_CONTENT_ALPHA),
            ),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(R.string.send),
            )
        }
    }
}

@Composable
private fun DateSeparator(date: LocalDate) {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    val dateText = when (date) {
        today -> stringResource(R.string.today)
        yesterday -> stringResource(R.string.yesterday)
        else -> date.format(
            DateTimeFormatter.ofPattern(
                "d MMMM",
                LocalLocale.current.platformLocale
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Spacing.small),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape( Dimens.Shape.medium),
            color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = DATE_SEPARATOR_ALPHA)
        ) {
            Text(
                text = dateText,
                modifier = Modifier.padding(
                    horizontal = Dimens.Spacing.medium,
                    vertical =  Dimens.Spacing.small
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}