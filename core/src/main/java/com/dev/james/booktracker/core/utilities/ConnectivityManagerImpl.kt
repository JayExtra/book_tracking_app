package com.dev.james.booktracker.core.utilities

import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class ConnectivityManagerImpl(
    private val context: Context
) : ConnectivityManager {

    override fun getNetworkStatus(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkConnectivityVersionM(connectivityManager)
        }else{
            checkConnectivityBelowVersionM(connectivityManager)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkConnectivityVersionM(
        connectivityManager: android.net.ConnectivityManager
    ) : Boolean {
        val network = connectivityManager.activeNetwork
        val networkCapability = connectivityManager.getNetworkCapabilities(network)
        val isConnected = networkCapability?.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )
        return if( isConnected != null && isConnected == true){
            DoesNetworkHaveConnectivity.execute()
        }else{
            false
        }
    }

    private fun checkConnectivityBelowVersionM(
        connectivityManager: android.net.ConnectivityManager
    ) : Boolean{
        val networkInfo = connectivityManager.activeNetworkInfo
        return if(networkInfo != null && networkInfo.isConnected){
            DoesNetworkHaveConnectivity.execute()
        }else{
            false
        }
    }
}

object DoesNetworkHaveConnectivity {
    fun execute() : Boolean {
        return try{
            Timber.tag("ConnectivityManager").d("PINGING google...")
            val socket = Socket()
            socket.connect(
                InetSocketAddress("8.8.8.8" ,53),
                1500
            )
            socket.close()
            Timber.tag("ConnectivityManager").d("PING success!")
            true
        }catch (e : IOException){
            Timber.tag("ConnectivityManager").d("No internet connection ${e.message}")
            false
        }
    }
}