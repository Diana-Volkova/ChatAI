package com.example.chatai.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val preferences: SharedPreferences
) {
    fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        preferences.edit {
            putString("access_token", accessToken)
                .putString("refresh_token", refreshToken)
        }
    }

    fun accessToken(): String? =
        preferences.getString(
            "access_token",
            null
        )

    fun refreshToken(): String? =
        preferences.getString(
            "refresh_token",
            null
        )

    fun saveAccessToken(
        token: String
    ) {
        preferences.edit {
            putString(
                "access_token",
                token
            )
        }
    }

    fun clear() {
        preferences.edit {
            clear()
        }
    }
}