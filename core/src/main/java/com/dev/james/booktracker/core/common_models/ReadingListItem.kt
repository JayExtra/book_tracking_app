package com.dev.james.booktracker.core.common_models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey


data class ReadingListItem(
    val id : String ,
    val name : String,
    val image : String,
    val description : String,
    val readingList : List<String>,
    val date : String ,
    val starred : Boolean
)
