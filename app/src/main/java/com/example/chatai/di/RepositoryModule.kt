package com.example.chatai.di

import com.example.chatai.data.ChatApi
import com.example.chatai.data.local.MessageDao
import com.example.chatai.data.repository.ChatRepositoryImpl
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
    fun provideChatRepository(
        api: ChatApi,
        dao: MessageDao
    ): ChatRepositoryImpl {
        return ChatRepositoryImpl(api, dao)
    }
}