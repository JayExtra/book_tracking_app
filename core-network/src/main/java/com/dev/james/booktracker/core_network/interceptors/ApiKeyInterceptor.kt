package com.dev.james.booktracker.core_network.interceptors

import com.dev.james.booktracker.core_network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKey = BuildConfig.GOOGLE_API_KEY
        val request = chain.request()
            .newBuilder()
            .url(
                chain.request()
                    .url.newBuilder()
                    .addQueryParameter("key" , apiKey)
                    .build()
            )
            .build()
        return chain.proceed(request)
    }
}