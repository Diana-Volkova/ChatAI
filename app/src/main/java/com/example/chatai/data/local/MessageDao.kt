package com.example.chatai.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: MessageEntity): Long

    @Query(" SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC, id ASC")
    suspend fun getByChatId(chatId: Int): List<MessageEntity>

    @Query("DELETE FROM messages WHERE chatId = :chatId ")
    suspend fun clearChat( chatId: Int)

    @Query("SELECT * FROM messages WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): MessageEntity?

    @Query("UPDATE messages SET serverId = :serverId WHERE id = :localId")
    suspend fun updateServerId(
        localId: Long,
        serverId: Long
    )

    @Query("DELETE FROM messages WHERE serverId = :serverId")
    suspend fun deleteByServerId(serverId: Long)

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM messages WHERE serverId IN (:serverIds)")
    suspend fun deleteByServerIds(serverIds: List<Long>)

    @Query("DELETE FROM messages")
    suspend fun clearAll()

    @Query("SELECT * FROM messages WHERE chatId = :chatId AND serverId = :serverId LIMIT 1")
    suspend fun getByServerId(chatId: Int, serverId: Long): MessageEntity?
}