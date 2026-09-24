package com.example.chatai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE messages ADD COLUMN serverId INTEGER"
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE chat_settings (
                chatId INTEGER NOT NULL,
                theme TEXT NOT NULL,
                PRIMARY KEY(chatId)
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE chats (
                chatId INTEGER NOT NULL,
                title TEXT NOT NULL,
                model TEXT NOT NULL,
                lastMessageAt INTEGER,
                PRIMARY KEY(chatId)
            )
            """.trimIndent()
        )
    }
}

@Database(
    entities = [
        MessageEntity::class,
        ChatEntity::class,
        ChatSettingsEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    abstract fun chatDao() : ChatDao
    abstract fun settingsDao() : ChatSettingsDao
}