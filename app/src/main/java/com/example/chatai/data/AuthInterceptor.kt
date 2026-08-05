package com.example.chatai.data

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {
        val token = sessionManager.accessToken()
        val request =
            chain.request()
                .newBuilder()
                .apply {
                    if (token != null) {
                        addHeader(
                            "Authorization",
                            "Bearer $token"
                        )
                    }
                }
                .build()

        Log.d(
            "HTTP",
            "request ${chain.request().url}"
        )

        return chain.proceed(request)
    }
}