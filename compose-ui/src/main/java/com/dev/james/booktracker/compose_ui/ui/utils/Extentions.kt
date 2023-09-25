package com.dev.james.booktracker.compose_ui.ui.utils

import android.content.Context
import java.security.SecureRandom
import java.util.UUID

fun Long.formatTimeToDHMS() : String {

    val units = arrayOf("d", "h", "m", "s")
    val values = longArrayOf(this / 86400000L,
        this % 86400000L / 3600000L, this % 86400000 % 3600000 / 60000,
        this % 86400000L % 3600000L % 60000L / 1000L)

    val timeStringBuilder = StringBuilder()

    for (i in units.indices) {
        if (values[i] > 0) {
            timeStringBuilder.append("${values[i]}${units[i]} ")
        }
    }
    val formattedTime = timeStringBuilder.toString()

    return formattedTime.ifBlank { "0s" }
}