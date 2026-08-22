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

@Database(
    entities = [
        MessageEntity::class,
        ChatSettingsEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao
    abstract fun settingsDao() : ChatSettingsDao
}