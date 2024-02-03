package com.dev.james.booktracker.core.entities.updates

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookLogUpdate(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("log_id")
    val logId : String ,
    @ColumnInfo("period_taken")
    val period : Long ,
    @ColumnInfo("pages_read")
    val pagesRead : Int ,
    @ColumnInfo("current_chapter_title")
    val currentChapterTitle : String ,
    @ColumnInfo("current_chapter")
    val currentChapter : Int ,
    @ColumnInfo("current_page")
    val currentPage : Int
)
