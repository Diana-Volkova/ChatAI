package com.example.chatai.presentation.ui.sync

sealed interface SyncStatus {

    data object Synced : SyncStatus

    data object OfflineCached : SyncStatus
}