package com.dev.james.booktracker.core_database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books_table")
data class BookEntity(
    @ColumnInfo("id")
    @PrimaryKey(autoGenerate = false)
    val bookId : String,
    @ColumnInfo("book_image")
    val bookImage : String,
    @ColumnInfo("book_title")
    val bookTitle : String,
    @ColumnInfo("book_authors")
    val bookAuthors : List<String>,
    @ColumnInfo("book_thumbnail")
    val bookThumbnail : String,
    @ColumnInfo("book_pages")
    val bookPagesCount : Int ,
    @ColumnInfo("publisher_name")
    val publisher : String ,
    @ColumnInfo("published_date")
    val publishedDate : String
)
