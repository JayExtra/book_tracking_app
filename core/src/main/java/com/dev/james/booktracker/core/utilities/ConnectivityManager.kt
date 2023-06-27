package com.dev.james.booktracker.core.utilities

interface ConnectivityManager {
    fun getNetworkStatus() : Boolean
}