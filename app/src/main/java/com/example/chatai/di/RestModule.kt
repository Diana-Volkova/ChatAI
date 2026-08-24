package com.example.chatai.di

import android.content.Context
import android.content.SharedPreferences
import com.example.chatai.R
import com.example.chatai.data.remote.api.AuthApi
import com.example.chatai.data.remote.interceptor.AuthInterceptor
import com.example.chatai.data.remote.api.ChatApi
import com.example.chatai.data.remote.authenticator.JwtAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestModule {

    @Provides
    @Singleton
    fun provideBaseUrl(
        @ApplicationContext context: Context
    ): String {
        return "http://${context.getString(R.string.backend_ip)}:8001/"
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

    // ---------- AUTH API (без токенов) ----------

    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthRetrofit(
        baseUrl: String
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }


    @Provides
    @Singleton
    fun provideAuthApi(
        @Named("authRetrofit")
        retrofit: Retrofit
    ): AuthApi {

        return retrofit.create(
            AuthApi::class.java
        )
    }


    // ---------- CHAT API (с JWT) ----------

    @Provides
    @Singleton
    fun provideChatClient(
        authInterceptor: AuthInterceptor,
        jwtAuthenticator: JwtAuthenticator
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(jwtAuthenticator)
            .build()
    }


    @Provides
    @Singleton
    @Named("chatRetrofit")
    fun provideChatRetrofit(
        client: OkHttpClient,
        baseUrl: String
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }


    @Provides
    @Singleton
    fun provideChatApi(
        @Named("chatRetrofit")
        retrofit: Retrofit
    ): ChatApi {

        return retrofit.create(
            ChatApi::class.java
        )
    }
}