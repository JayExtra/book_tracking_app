package com.dev.james.booktracker.core.utilities

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val field: String? = null,
    val code: Int? = null
) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null, field: String? = null, code: Int? = null) : Resource<T>(data, message, field, code)
    class Loading<T>(data: T? = null, status: Boolean = false) : Resource<T>(data)
}