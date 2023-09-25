package com.dev.james.book_tracking.presentation.domain

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class StopWatch {

    private var time : Duration = Duration.ZERO
    private lateinit var timer : Timer


    private var seconds by mutableStateOf("00")
    private var minutes by mutableStateOf("00")
    private var hours by mutableStateOf("00")
    private var isActive = false
    var formattedTime by mutableStateOf("00:00:00")

    companion object{
        const val TAG = "StopWatch"
    }

    fun start() {
        if(isActive) return
        timer = fixedRateTimer(initialDelay = 1000L , period = 1000L){
            time = time.plus(1.seconds)
            updateTimeStates()
            formattedTime = "$hours:$minutes:$seconds"
        }
    }

    private fun updateTimeStates(){
        time.toComponents{ _ , hours, minutes , seconds, _ ->
            this@StopWatch.seconds = seconds.pad()
            this@StopWatch.minutes = minutes.pad()
            this@StopWatch.hours = hours.pad()
        }
    }

    private fun Int.pad() : String{
        return this.toString().padStart(2 , '0')
    }

    fun pause() {
        isActive = false
        timer.cancel()
    }

    fun reset() {
        pause()
        formattedTime = "00:00:00"
        time = Duration.ZERO
        updateTimeStates()
    }

}