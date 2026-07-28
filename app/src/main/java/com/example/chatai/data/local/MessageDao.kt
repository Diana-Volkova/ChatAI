package com.example.chatai.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {

    @Insert
    suspend fun insert(message: MessageEntity)

    @Query("SELECT * FROM messages ORDER BY timestamp DESC, id DESC LIMIT 20")
    suspend fun getAll(): List<MessageEntity>

    @Query("DELETE FROM messages")
    suspend fun clear()
}