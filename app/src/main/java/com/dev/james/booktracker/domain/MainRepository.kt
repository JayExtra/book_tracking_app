package com.dev.james.booktracker.domain

interface MainRepository {
    suspend fun getOnBoardingStatus() : Boolean
}