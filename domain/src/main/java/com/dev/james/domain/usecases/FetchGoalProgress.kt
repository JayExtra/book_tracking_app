package com.dev.james.domain.usecases

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.dev.james.booktracker.core.common_models.GoalLog
import com.dev.james.booktracker.core.common_models.GoalProgressData
import com.dev.james.booktracker.core.utilities.formatToDateString
import com.dev.james.booktracker.core.utilities.getDateRange
import com.dev.james.booktracker.core.utilities.getDayString
import com.dev.james.booktracker.core.utilities.getWeekRange
import com.dev.james.booktracker.core.utilities.toAppropriateDay
import com.dev.james.domain.repository.home.GoalsRepository
import com.dev.james.domain.repository.home.LogsRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

class FetchGoalProgress @Inject constructor(
    private val goalsRepository: GoalsRepository ,
    private val goalLogsRepository: LogsRepository
) {
    companion object {
        const val TAG = "FetchGoalProgress"
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    suspend operator fun invoke() : GoalProgressData {
        val activeGoalsList = goalsRepository.getActiveGoals().first()
        return if(activeGoalsList.isEmpty()){
            GoalProgressData()
        }else{
            val activeGoal = activeGoalsList.first()
            val activeGoalLogs = fetchGoalLogs(activeGoal.goalId)

     /*       val testLogs = listOf(
                GoalLog(
                    parentGoalId = "siuhsdcs80" ,
                    id = "823sajhjhascas" ,
                    startTime = LocalDate.now() ,
                    endTime = LocalDate.now() ,
                    duration = 1800000L
                ) ,
                GoalLog(
                    parentGoalId = "siuhsdcs80" ,
                    id = "823sajhjhascas" ,
                    startTime = LocalDate.now().minusDays(2) ,
                    endTime = LocalDate.now().minusDays(2) ,
                    duration = 3600000L
                ) ,
                GoalLog(
                    parentGoalId = "siuhsdcs80" ,
                    id = "823sajhjhascas" ,
                    startTime = LocalDate.now().plusDays(2) ,
                    endTime = LocalDate.now().plusDays(2) ,
                    duration = 7200000L
                )

            )*/

            Timber.tag(TAG).d("Test graph data: ${mapDataToGraphData(activeGoalLogs)}")

            val weeklyLogData = mapDataToGraphData(activeGoalLogs)

            GoalProgressData(
                goalId = activeGoal.goalId ,
                goalInfo = activeGoal.goalInfo ,
                goalPeriod = activeGoal.goalPeriod ,
                goalTime = activeGoal.goalTime ,
                booksToRead = activeGoal.booksToRead ,
                booksRead = activeGoal.booksRead ,
                weeklyLogData = weeklyLogData
            )
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mapDataToGraphData(goalLogs : List<GoalLog>) : Map<String , Long> {
        val weekRange = getWeekRange()
        val mappedLogs = mutableMapOf<String , Long>()
        val finalLog = mutableMapOf<String , Long>()

        return if (goalLogs.isNotEmpty()){
            goalLogs.forEach { log ->
                log.startTime?.formatToDateString()?.let { mappedLogs[it] = log.duration }
            }
            weekRange.forEach { date ->
                if(mappedLogs.containsKey(date)){
                    mappedLogs[date]?.let { finalLog.put( date.getDayString().toAppropriateDay() , it) }
                }else{
                    finalLog[date.getDayString().toAppropriateDay()] = 0L
                }
            }
           finalLog
        }else {
            weekRange.forEach { date ->
                finalLog[date.getDayString().toAppropriateDay()] = 0L
            }
            finalLog
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun fetchGoalLogs(goalId : String ) : List<GoalLog>{
        val dateRange = getDateRange()
        return goalLogsRepository.getGoalLogs(parentLogId = goalId, startDate = dateRange.startDate , endDate = dateRange.endDate)
    }
}