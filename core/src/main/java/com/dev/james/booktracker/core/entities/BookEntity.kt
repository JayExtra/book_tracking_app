package com.dev.james.booktracker.core.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books_table")
data class BookEntity(
    @ColumnInfo("id")
    @PrimaryKey(autoGenerate = false)
    val bookId : String,
    @ColumnInfo("book_image_url")
    val bookImage : String,
    @ColumnInfo("is_uri")
    val isUri : Boolean ,
    @ColumnInfo("book_title")
    val bookTitle : String,
    @ColumnInfo("book_authors")
    val bookAuthors : String,
    @ColumnInfo("book_small_thumbnail")
    val bookSmallThumbnail : String,
    @ColumnInfo("book_pages")
    val bookPagesCount : Int ,
    @ColumnInfo("publisher_name")
    val publisher : String ,
    @ColumnInfo("published_date")
    val publishedDate : String ,
   /* @ColumnInfo("current_chapter")
    val currentChapter : Int ,
    @ColumnInfo("current_chapter_title")
    val currentChapterTitle : String ,*/
    @ColumnInfo("total_chapters")
    val chapters : Int
)
