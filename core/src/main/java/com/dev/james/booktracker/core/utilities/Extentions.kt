package com.dev.james.booktracker.core.utilities

fun <T> List<T>.convertToAuthorsString() : String {
    return this.toString().drop(1).dropLast(1)
}