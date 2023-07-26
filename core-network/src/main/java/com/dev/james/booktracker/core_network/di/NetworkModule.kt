package com.dev.james.booktracker.core_network.di

import com.dev.james.booktracker.core_network.BuildConfig
import com.dev.james.booktracker.core_network.api_service.BooksApi
import com.dev.james.booktracker.core_network.utilities.Endpoints.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    @Provides
    @Singleton
    fun provideRetrofitClient() : OkHttpClient {
        val client = OkHttpClient.Builder()
            .connectTimeout(5 , TimeUnit.SECONDS)
            .readTimeout(5 , TimeUnit.SECONDS)
            .writeTimeout(5 , TimeUnit.SECONDS)

        if(BuildConfig.DEBUG) client.addInterceptor(loggingInterceptor)

        return client.build()

    }

    @Provides
    @Singleton
    fun provideRetrofit(
        retrofitClient : OkHttpClient
    ) : Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(retrofitClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()


    @Provides
    @Singleton
    fun provideBooksApiService(retrofit: Retrofit) : BooksApi =
        retrofit.create(
            BooksApi::class.java
        )

}



