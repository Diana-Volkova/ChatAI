package com.example.chatai.presentation.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatai.domain.interactors.HistoryInteractor
import com.example.chatai.domain.interactors.MessageInteractor
import com.example.chatai.domain.model.ChatSettings
import com.example.chatai.domain.repository.ChatSettingsRepository
import com.example.chatai.domain.theme.ChatThemeId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val historyInteractor: HistoryInteractor,
    private val messageInteractor: MessageInteractor,
    private val chatSettingsRepository: ChatSettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow<ChatState>(ChatState.Loading)
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _settings = MutableStateFlow<ChatSettings?>(null)
    val settings = _settings.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Int>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    fun dispatch(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.LoadHistory -> {
                loadHistory(intent.chatId)
            }

            is ChatIntent.SendMessage -> {
                sendMessage(intent.chatId, intent.text)
            }

            is ChatIntent.DeleteMessages -> {
                deleteMessages(intent.chatId, intent.messageIds)
            }

            is ChatIntent.ClearHistory -> {
                clearHistory(intent.chatId)
            }

            is ChatIntent.SetTheme -> {
                setTheme(intent.chatId, intent.chatThemeId)
            }

            is ChatIntent.ObserveSettings -> {
                observeSettings(intent.chatId)
            }

            is ChatIntent.SearchMessages -> {
                searchMessages(intent.query)
            }
        }
    }

    private fun observeSettings(chatId: Int) {
        viewModelScope.launch {
            chatSettingsRepository
                .observe(chatId)
                .collect { settings ->
                    _settings.value = settings
                }
        }
    }


    private fun setTheme(chatId: Int, chatThemeId: ChatThemeId) {
        viewModelScope.launch {
            chatSettingsRepository.setTheme(chatId, chatThemeId)
        }
    }

    private fun sendMessage(chatId: Int, text: String) {
        viewModelScope.launch {
            try {
                messageInteractor.sendMessage(chatId, text)
            } catch (e: Exception) {
                _state.value = ChatState.Error(e.message ?: "error")
            }
        }
    }

    private fun loadHistory(chatId: Int) {
        viewModelScope.launch {
            try {
                historyInteractor
                    .observeHistory(chatId)
                    .onStart {
                        historyInteractor.syncHistory(chatId)
                    }
                    .collect { history ->
                        _state.value = ChatState.Success(history)
                    }
            } catch (e: Exception) {
                _state.value = ChatState.Error(e.message ?: "error")
            }
        }
    }

    private fun deleteMessages(chatId: Int, messageIds: List<Long>) {
        viewModelScope.launch {
            try {
                messageInteractor.deleteMessages(chatId, messageIds)
            } catch (e: Exception) {
                _state.value = ChatState.Error(e.message ?: "error")
            }
        }
    }

    private fun clearHistory(chatId: Int) {
        viewModelScope.launch {
            try {
                historyInteractor.clearHistory(chatId)
                _state.value = ChatState.Success(emptyList())
            } catch (e: Exception) {
                _state.value = ChatState.Error(e.message ?: "error")
            }
        }
    }

    private fun searchMessages(query: String) {
        _searchQuery.value = query

        val state = _state.value

        if (state !is ChatState.Success || query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        _searchResults.value = state.messages
            .mapIndexedNotNull { index, message ->
                if (
                    message.text.contains(
                        query,
                        ignoreCase = true
                    )
                ) {
                    index
                } else {
                    null
                }
            }
    }
}