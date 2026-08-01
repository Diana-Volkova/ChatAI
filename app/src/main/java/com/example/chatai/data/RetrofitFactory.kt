package com.example.chatai.data

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitFactory {

    private lateinit var preferences: SharedPreferences

    fun init(sharedPreferences: SharedPreferences) {
        preferences = sharedPreferences
    }

    private val interceptor = Interceptor { chain ->

        val requestBuilder = chain.request()
            .newBuilder()

        val token = preferences.getString("access_token", null)

        if (token != null) {
            requestBuilder.addHeader(
                "Authorization",
                "Bearer $token"
            )
        }

        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    fun retrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
}