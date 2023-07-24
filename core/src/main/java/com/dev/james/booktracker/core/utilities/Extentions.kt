package com.dev.james.booktracker.core.utilities

import java.util.UUID

fun <T> List<T>.convertToAuthorsString() : String {
    return this.toString().drop(1).dropLast(1)
}

fun generateSecureUUID() : String {
    return UUID.randomUUID().toString()
}