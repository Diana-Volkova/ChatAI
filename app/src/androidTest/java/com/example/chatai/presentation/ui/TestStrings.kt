package com.example.chatai.presentation.ui

import androidx.test.platform.app.InstrumentationRegistry
import com.example.chatai.R

object TestStrings {
    private val context = InstrumentationRegistry
        .getInstrumentation()
        .targetContext

    val logIn = context.getString(R.string.login)

    val logInTitle = context.getString(R.string.login_title)

    val forgotPassword = context.getString(R.string.forgot_password)
    val email = context.getString(R.string.email)
    val password = context.getString(R.string.password)
    val registration = context.getString(R.string.registration)
    val userName = context.getString(R.string.user_name)
    val repeatPassword = context.getString(R.string.repeat_password)
    val createAccount = context.getString(R.string.create_account)
    val alreadyHaveAccount = context.getString(R.string.already_have_account)
}