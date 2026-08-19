package com.example.chatai.di

import android.content.Context
import androidx.room.Room
import com.example.chatai.data.local.ChatDatabase
import com.example.chatai.data.local.MIGRATION_1_2
import com.example.chatai.data.local.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ChatDatabase {
        return Room.databaseBuilder(
            context,
            ChatDatabase::class.java,
            "chat.db"
        ).addMigrations(MIGRATION_1_2).build()
    }

    @Provides
    fun provideMessageDao(
        database: ChatDatabase
    ): MessageDao {
        return database.messageDao()
    }
}