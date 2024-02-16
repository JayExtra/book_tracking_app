package com.dev.james.booktracker.core.entities.updates

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GoalUpdate(
    @ColumnInfo("id")
    @PrimaryKey(autoGenerate = false)
    val id : String,
    @ColumnInfo("books_read")
    val booksRead : Int ,
)
