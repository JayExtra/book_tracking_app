package com.dev.james.booktracker.on_boarding.domain

interface OnBoardingRepository {
    suspend fun updateOnBoardingStatus(status : Boolean)
    suspend fun getOnBoardingStatus() : Boolean
}