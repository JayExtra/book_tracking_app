package com.dev.james.domain.usecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.dev.james.booktracker.core.utilities.calculateDaysPast
import com.dev.james.booktracker.core.utilities.checkDaysTaken
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import com.dev.james.domain.repository.home.LogsRepository
import javax.inject.Inject

class FetchOrSetStreak @Inject constructor(
    private val logsRepository: LogsRepository ,
    private val dataStoreManager: DataStoreManager
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetch(onStreakReset : (Boolean) -> Unit = {}) : Int {

        val mostRecentLog = logsRepository.getRecentGoalLog()

        return if(mostRecentLog.id.isNotBlank()){
            val logDate = mostRecentLog.startTime
            val daysBetween = logDate?.calculateDaysPast()
            return if(daysBetween!! > 2) {
                resetStreak()
                onStreakReset(true)
                0
            }else{
                dataStoreManager.readIntValueOnce(DataStorePreferenceKeys.CURRENT_STREAK)
            }
        }else {
            0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun setStreak() {
        val currentStreak = dataStoreManager.readIntValueOnce(DataStorePreferenceKeys.CURRENT_STREAK)
        dataStoreManager.storeIntValue(DataStorePreferenceKeys.CURRENT_STREAK , currentStreak + 1)
    }

    private suspend fun resetStreak(){
        dataStoreManager.storeIntValue(DataStorePreferenceKeys.CURRENT_STREAK , 0 )
    }

}