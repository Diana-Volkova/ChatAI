package com.example.chatai.data

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class JwtAuthenticator @Inject constructor(
    private val sessionManager: SessionManager,
    private val authApi: AuthApi
) : Authenticator {

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {

        Log.d(
            "JWT",
            "AUTHENTICATOR CALLED ${response.code}"
        )

        if (responseCount(response) >= 2) {
            return null
        }

        val refreshToken =
            sessionManager.refreshToken()

        if (refreshToken == null) {

            Log.d(
                "JWT",
                "NO REFRESH TOKEN"
            )

            return null
        }

        Log.d(
            "JWT",
            "refreshToken=$refreshToken"
        )

        val refreshResponse =
            runBlocking {
                authApi.refresh(
                    RefreshRequest(
                        refreshToken
                    )
                )
            }

        Log.d(
            "JWT",
            "refresh response ${refreshResponse.code()}"
        )



        if (!refreshResponse.isSuccessful) {
            Log.e(
                "JWT",
                "refresh failed ${refreshResponse.code()} ${
                    refreshResponse.errorBody()?.string()
                }"
            )

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