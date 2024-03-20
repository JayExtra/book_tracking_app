package com.dev.james.booktracker.core.entities.updates

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReadingListBookUpdate(
    @PrimaryKey(autoGenerate = false)
    val id : String ,
    @ColumnInfo(name = "reading_list")
    val readingList : List<String>
)
