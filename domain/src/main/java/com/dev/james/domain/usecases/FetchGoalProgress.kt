package com.dev.james.domain.usecases

import com.dev.james.booktracker.core.common_models.GoalLog
import com.dev.james.booktracker.core.common_models.GoalProgressData
import com.dev.james.domain.repository.home.GoalsRepository
import com.dev.james.domain.repository.home.LogsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FetchGoalProgress @Inject constructor(
    private val goalsRepository: GoalsRepository ,
    private val goalLogsRepository: LogsRepository
) {

    suspend operator fun invoke() : GoalProgressData {
        val activeGoalsList = goalsRepository.getActiveGoals().first()
        return if(activeGoalsList.isEmpty()){
            GoalProgressData()
        }else{
            val activeGoal = activeGoalsList.first()
            val activeGoalLogs = fetchGoalLogs(activeGoal.goalId)

            GoalProgressData(
                goalId = activeGoal.goalId ,
                goalInfo = activeGoal.goalInfo ,
                goalPeriod = activeGoal.goalPeriod ,
                goalTime = activeGoal.goalTime ,
                booksToRead = activeGoal.booksToRead ,
                booksRead = activeGoal.booksRead ,
                goalLogsList = activeGoalLogs
            )
        }

    }

    private suspend fun fetchGoalLogs(goalId : String ) : List<GoalLog>{
        return goalLogsRepository.getGoalLogs(goalId = goalId)
            .first()
    }
}