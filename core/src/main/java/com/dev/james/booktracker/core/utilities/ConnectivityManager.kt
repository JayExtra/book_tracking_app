package com.dev.james.booktracker.core.utilities

interface ConnectivityManager {
    suspend fun getNetworkStatus() : Boolean
}