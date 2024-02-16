package com.dev.james.domain.usecases

import com.dev.james.domain.repository.home.GoalsRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class UpdateGoalUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository
) {
    companion object {
        const val TAG = "UpdateGoalUseCase"
    }
    suspend fun addBooksReadTotal(){
        val goal = goalsRepository.getActiveGoals().first()
        if(goal.isNotEmpty()){
            val goalId = goal[0].goalId
            val totalBooksRead = goal[0].booksRead
            goalsRepository.updateBooksRead(
                goalId = goalId ,
                bookCount = totalBooksRead + 1
            )
        }else {
            Timber.tag(TAG).d("addBooksReadTotal:no goals found in db")
        }
    }

    suspend fun subtractBooksReadTotal(){
        val goal = goalsRepository.getActiveGoals().first()
        if(goal.isNotEmpty()){
            val goalId = goal[0].goalId
            val totalBooksRead = goal[0].booksRead
            if(totalBooksRead > 0){
                goalsRepository.updateBooksRead(
                    goalId = goalId ,
                    bookCount = totalBooksRead - 1
                )
            }
        }else {
            Timber.tag(TAG).d("subtractBooksReadTotal:no goals found in db")
        }
    }
}