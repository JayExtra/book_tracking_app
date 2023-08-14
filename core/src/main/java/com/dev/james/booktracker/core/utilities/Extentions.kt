package com.dev.james.booktracker.core.utilities

import java.security.SecureRandom
import java.util.UUID

fun <T> List<T>.convertToAuthorsString() : String {
    return this.toString().drop(1).dropLast(1)
}

fun String.calculateTimeToLong() : Long {
    val stringArray = this.split(" ")
    return if(stringArray.size > 1 ){
        val hourInMillis = stringArray[0].filter { it.isDigit() }.toLong() * 3600000L
        val minutesInMillis = stringArray[1].filter { it.isDigit() }.toLong() * 60000L
        hourInMillis + minutesInMillis
    }else{
        val timeString = stringArray[0]
        return if (timeString.isHour()){
            timeString.filter { it.isDigit() }.toLong() * 3600000L
        }else{
            timeString.filter { it.isDigit() }.toLong() * 60000L
        }
    }
}

fun String.isHour() : Boolean {
    val firstChar = this.filter { it.isLetter() }.first()
    return firstChar == 'h'
}

fun generateSecureUUID() : String {
    return UUID.randomUUID().toString()
}

fun generateRandomId(length: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val random = SecureRandom()
    val sb = StringBuilder(length)

    for (i in 0 until length) {
        val randomIndex = random.nextInt(chars.length)
        sb.append(chars[randomIndex])
    }

    return sb.toString()
}