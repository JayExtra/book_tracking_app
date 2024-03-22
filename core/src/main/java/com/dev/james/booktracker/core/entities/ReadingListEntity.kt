package com.dev.james.booktracker.core.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_lists")
data class ReadingListEntity(
    @PrimaryKey(autoGenerate = false)
    val id : String ,
    val name : String ,
    val image : String ,
    val description : String ,
    @ColumnInfo(name = "reading_list")
    val readingList : List<String>,
    @ColumnInfo(name = "date_created")
    val date : String ,
    val starred : Boolean
)
