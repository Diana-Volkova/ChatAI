package com.example.chatai.di

import com.example.chatai.data.local.ChatDao
import com.example.chatai.data.local.ChatSettingsDao
import com.example.chatai.data.remote.api.ChatApi
import com.example.chatai.data.local.MessageDao
import com.example.chatai.data.local.SessionManager
import com.example.chatai.data.repository.AuthRepositoryImpl
import com.example.chatai.data.repository.ChatRepositoryImpl
import com.example.chatai.data.repository.ChatSettingsRepositoryImpl
import com.example.chatai.data.repository.MessageRepositoryImpl
import com.example.chatai.domain.repository.AuthRepository
import com.example.chatai.domain.repository.ChatRepository
import com.example.chatai.domain.repository.ChatSettingsRepository
import com.example.chatai.domain.repository.MessageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: ChatApi,
        sessionManager: SessionManager,
    ): AuthRepository {
        return AuthRepositoryImpl(api, sessionManager)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        api: ChatApi,
        dao: ChatDao
    ): ChatRepository {
        return ChatRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(
        api: ChatApi,
        dao: MessageDao
    ): MessageRepository {
        return MessageRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideChatSettingsRepository(
        dao: ChatSettingsDao
    ): ChatSettingsRepository {
        return ChatSettingsRepositoryImpl(dao)
    }
}