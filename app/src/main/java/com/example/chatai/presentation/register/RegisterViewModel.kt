package com.example.chatai.presentation.register

import androidx.lifecycle.ViewModel
import com.example.chatai.data.ChatApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val api: ChatApi
) : ViewModel() {
    fun dispatch(intent: RegisterIntent) {

    }
}