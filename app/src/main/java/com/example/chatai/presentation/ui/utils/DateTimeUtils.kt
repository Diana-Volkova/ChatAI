package com.example.chatai.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.chatai.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
fun formatTime(timestamp: Long): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault())
        .format(Date(timestamp))
}

@Composable
fun formatLastMessageTime(timestamp: Long?): String {
    if (timestamp == null) return ""

    val timeZone = TimeZone.currentSystemDefault()

    val messageDateTime = Instant
        .fromEpochMilliseconds(timestamp)
        .toLocalDateTime(timeZone)

    val today = Clock.System.now()
        .toLocalDateTime(timeZone)
        .date

    val messageDate = messageDateTime.date

    return when {
        messageDate == today -> {
            "%02d:%02d".format(
                messageDateTime.hour,
                messageDateTime.minute
            )
        }

        messageDate == today.minus(DatePeriod(days = 1)) -> {
            stringResource(R.string.yesterday)
        }

        messageDate.year == today.year -> {
            "${messageDate.dayOfMonth} ${monthName(messageDate.month)}"
        }

        else -> {
            "%02d.%02d.%04d".format(
                messageDate.dayOfMonth,
                messageDate.monthNumber,
                messageDate.year
            )
        }
    }
}

@Composable
private fun monthName(month: Month): String {
    return when (month) {
        Month.JANUARY -> stringResource(R.string.month_january)
        Month.FEBRUARY -> stringResource(R.string.month_february)
        Month.MARCH -> stringResource(R.string.month_march)
        Month.APRIL -> stringResource(R.string.month_april)
        Month.MAY -> stringResource(R.string.month_may)
        Month.JUNE -> stringResource(R.string.month_june)
        Month.JULY -> stringResource(R.string.month_july)
        Month.AUGUST -> stringResource(R.string.month_august)
        Month.SEPTEMBER -> stringResource(R.string.month_september)
        Month.OCTOBER -> stringResource(R.string.month_october)
        Month.NOVEMBER -> stringResource(R.string.month_november)
        Month.DECEMBER -> stringResource(R.string.month_december)
    }
}