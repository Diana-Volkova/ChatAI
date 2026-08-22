package com.example.chatai.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatSettingsDao {

    @Query("SELECT * FROM chat_settings WHERE chatId = :chatId")
    suspend fun get(chatId: Int): ChatSettingsEntity?

    @Query("SELECT * FROM chat_settings WHERE chatId = :chatId")
    fun observe(chatId: Int): Flow<ChatSettingsEntity?>

    @Upsert
    suspend fun upsert(settings: ChatSettingsEntity)
}