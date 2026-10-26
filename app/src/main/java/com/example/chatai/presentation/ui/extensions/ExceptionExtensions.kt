package com.example.chatai.presentation.ui.extensions

import android.content.Context
import com.example.chatai.R
import com.example.chatai.domain.exception.AuthException
import com.example.chatai.domain.exception.ChatException
import java.net.SocketTimeoutException

fun Exception.getLocalizedMessage(context: Context): String =
    when (this) {
        is AuthException -> context.getString(
            when (code) {
                400 -> R.string.auth_exception_bad_request
                401 -> R.string.auth_exception_unauthorized
                403 -> R.string.auth_exception_forbidden
                404 -> R.string.auth_exception_not_found
                409 -> R.string.auth_exception_conflict
                422 -> R.string.auth_exception_unprocessable
                429 -> R.string.auth_exception_rate_limit
                500 -> R.string.auth_exception_server
                502, 503, 504 -> R.string.auth_exception_unavailable
                else -> R.string.auth_exception_unknown
            },
            *if (code !in setOf(
                    400, 401, 403, 404, 409, 422, 429, 500, 502, 503, 504
                )
            ) arrayOf(code) else emptyArray()
        )

        is ChatException -> when (val errorCode = code) {
            401 -> context.getString(R.string.auth_exception_unauthorized)
            500 -> context.getString(R.string.auth_exception_server)
            null -> message
                ?: context.getString(R.string.chat_exception_unknown)
            else -> context.getString(
                R.string.chat_exception_unknown_code,
                errorCode
            )
        }

        is SocketTimeoutException ->
            context.getString(R.string.exception_no_internet)

        else -> message
            ?: context.getString(R.string.chat_exception_unknown)
    }