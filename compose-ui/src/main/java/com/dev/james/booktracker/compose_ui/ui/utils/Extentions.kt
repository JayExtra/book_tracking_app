package com.dev.james.booktracker.compose_ui.ui.utils

import android.content.Context
import timber.log.Timber
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

fun String.splitToDHMS() : String {
    val stringArray = this.trim().split(" ")
    Timber.tag("Extentions").d(stringArray.toString())
    when (stringArray.size) {
        1 -> {
            return stringArray[0]
        }
        2 -> {
            return stringArray[0]
        }
        3 -> {
            return stringArray[0] + stringArray[1]
        }
        4 -> {
            return stringArray[0] + stringArray[2] + stringArray[3]
        }
        else -> {
           return "0s"
        }
    }
}