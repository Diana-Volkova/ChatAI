package com.example.chatai.presentation.ui.password_restore

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class PasswordRestoreViewModel@Inject constructor(

) : ViewModel() {

    private val _effects = MutableSharedFlow<PasswordRestoreEffect>()

    val effects = _effects.asSharedFlow()

    fun dispatch(sendResetLink: Any) {}
}