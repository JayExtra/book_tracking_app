package com.dev.james.booktracker.core.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity("book_logs")
data class BookLogsEntity(
    @ColumnInfo("book_id")
    val bookId : String,
    @ColumnInfo("log_id")
    @PrimaryKey(autoGenerate = false)
    val logId : String,
    @ColumnInfo("started_time")
    val startedTime : LocalDate,
    @ColumnInfo("end_time")
    val endTime : LocalDate,
    @ColumnInfo("period_taken")
    val period : Long ,
    @ColumnInfo("pages_read")
    val pagesRead : Int ,
    @ColumnInfo("current_chapter_title")
    val currentChapterTitle : String ,
    @ColumnInfo("current_chapter")
    val currentChapter: Int,
    @ColumnInfo("current_page")
    val currentPage : Int
)
