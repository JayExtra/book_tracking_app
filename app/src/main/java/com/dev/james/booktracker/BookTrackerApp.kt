package com.dev.james.booktracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BookTrackerApp : Application(){
    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    private fun setupTimber(){
        Timber.plant(Timber.DebugTree())
    }
}
