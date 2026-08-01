package com.example.chatai.domain.di

import android.content.Context
import android.content.SharedPreferences
import com.example.chatai.data.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.chatai.data.ChatApi
import com.example.chatai.data.RetrofitFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestModule {
    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        interceptor: AuthInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    @Provides
    @Singleton
    fun provideChatRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return RetrofitFactory.create(
            baseUrl = "http://31.56.146.253:8001/",
            okHttpClient = okHttpClient
        )
    }

    @Provides
    @Singleton
    fun providePreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            "app_preferences",
            Context.MODE_PRIVATE
        )
    }
}