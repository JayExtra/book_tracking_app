package com.dev.james.booktracker.core.utilities

import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class ConnectivityManagerImpl(
    private val context: Context
) : ConnectivityManager {

    companion object {
        const val TAG = "ConnectivityManagerImpl"
    }

    override suspend fun getNetworkStatus(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkConnectivityVersionM(connectivityManager)
        } else {
            checkConnectivityBelowVersionM(connectivityManager)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun checkConnectivityVersionM(
        connectivityManager: android.net.ConnectivityManager
    ): Boolean {
        val network = connectivityManager.activeNetwork
        val networkCapability = connectivityManager.getNetworkCapabilities(network)
        
        return if(networkCapability == null){
            Timber.tag(TAG).d("The device is currently offline , no network")
            false
        }else {
            if(networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                hasNetwork(networkCapability)
            }else if(networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                hasNetwork(networkCapability)
            }else{
                Timber.tag(TAG).d("Unknown network detected!")
                false
            }
        }
    }

    private suspend fun hasNetwork(
        networkCapability: NetworkCapabilities
    ) : Boolean {
        val isConnected = networkCapability.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )
        val isDetected = networkCapability.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        )
        val canTransferData = networkCapability.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED
        )

        return if (isConnected && isDetected && canTransferData){
            DoesNetworkHaveConnectivity.execute()
        }else {
            false
        }
    }

    private suspend fun checkConnectivityBelowVersionM(
        connectivityManager: android.net.ConnectivityManager
    ): Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo
        if(networkInfo?.isConnectedOrConnecting == true && networkInfo.isAvailable){
            Timber.tag(TAG).d("device is online")
            return when(networkInfo.type){
                android.net.ConnectivityManager.TYPE_WIFI -> {
                    DoesNetworkHaveConnectivity.execute()
                }

                android.net.ConnectivityManager.TYPE_MOBILE -> {
                    DoesNetworkHaveConnectivity.execute()
                }

                else -> {
                    Timber.tag(TAG).d("Unknown network detected!")
                    false
                }
            }
        }else {
            return false
        }
    }
}

object DoesNetworkHaveConnectivity {
    suspend fun execute(): Boolean {
        return try {
            Timber.tag("ConnectivityManager").d("PINGING google...")
            val socket = Socket()
            withContext(Dispatchers.IO) {
                socket.connect(
                    InetSocketAddress("8.8.8.8", 53),
                    1500
                )
            }
            withContext(Dispatchers.IO) {
                socket.close()
            }
            Timber.tag("ConnectivityManager").d("PING success!")
            true
        } catch (e: IOException) {
            Timber.tag("ConnectivityManager").d("No internet connection ${e.message}")
            false
        }
    }
}