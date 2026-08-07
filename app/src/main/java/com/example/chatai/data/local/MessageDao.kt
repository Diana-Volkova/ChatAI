package com.example.chatai.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: MessageEntity)

    @Query(" SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC, id ASC")
    suspend fun getByChatId(chatId: Int): List<MessageEntity>

    @Query("DELETE FROM messages WHERE chatId = :chatId ")
    suspend fun clearChat( chatId: Int)
}