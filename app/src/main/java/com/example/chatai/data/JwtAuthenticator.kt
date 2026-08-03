package com.example.chatai.data

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class JwtAuthenticator @Inject constructor(
    private val sessionManager: SessionManager,
    private val authApi: AuthApi
) : Authenticator {


    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {


        if (responseCount(response) >= 2) {
            return null
        }


        val refreshToken =
            sessionManager.refreshToken()
                ?: return null


        val refreshResponse =
            runBlocking {

                authApi.refresh(
                    RefreshRequest(
                        refreshToken
                    )
                )
            }


        if (!refreshResponse.isSuccessful) {

            sessionManager.clear()

            return null
        }


        val newToken =
            refreshResponse.body()
                ?.access_token
                ?: return null


        sessionManager.saveAccessToken(
            newToken
        )


        return response.request
            .newBuilder()
            .header(
                "Authorization",
                "Bearer $newToken"
            )
            .build()
    }


    private fun responseCount(
        response: Response
    ): Int {

        var count = 1

        var previous =
            response.priorResponse

        while (previous != null) {
            count++
            previous =
                previous.priorResponse
        }

        return count
    }
}