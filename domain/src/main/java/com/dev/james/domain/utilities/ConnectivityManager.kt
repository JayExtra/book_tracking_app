package com.dev.james.domain.utilities

interface ConnectivityManager {
    suspend fun getNetworkStatus() : Boolean
}