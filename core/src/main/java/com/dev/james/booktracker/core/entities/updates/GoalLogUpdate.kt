package com.dev.james.booktracker.core.entities.updates

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GoalLogUpdate(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id : String ,
    @ColumnInfo("duration_taken")
    val duration : Long
)
