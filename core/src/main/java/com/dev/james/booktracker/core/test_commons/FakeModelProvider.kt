package com.dev.james.booktracker.core.test_commons

import com.dev.james.booktracker.core_database.room.entities.BookEntity
import com.dev.james.booktracker.core_database.room.entities.BookGoalsEntity
import com.dev.james.booktracker.core_database.room.entities.OverallGoalEntity
import com.dev.james.booktracker.core_database.room.entities.SpecificGoalsEntity
import com.dev.james.booktracker.core_network.dtos.BookDto
import com.dev.james.booktracker.core_network.dtos.BookVolumeDto
import com.dev.james.booktracker.core_network.dtos.Identifiers
import com.dev.james.booktracker.core_network.dtos.ImageLinks
import com.dev.james.booktracker.core_network.dtos.VolumeInfo
import com.squareup.moshi.Json

fun getTestBookEntity() = BookEntity(
    bookId = "abab12345",
    bookImage = "somebookimageurl",
    bookTitle = "Some book title",
    isUri = false,
    bookAuthors = "Author1 , Author2",
    bookSmallThumbnail = "Somesmallthumbnail",
    bookPagesCount = 122,
    publisher = "somepublisher",
    publishedDate = "12/10/2023",
    currentChapter = 11,
    chapters = 22,
    currentChapterTitle = "Some chapter title"
)

fun getTestOverallGoalEntity() = OverallGoalEntity(
    goalId = "test1234GOAL",
    goalInfo = "Read for 10 minutes every day",
    goalTime = 1600000L,
    goalPeriod = "Every day",
    specificDays = listOf(),
    shouldShowAlert = true,
    alertNote = "Some test note",
    isActive = true,
    alertTime = "Friday 12:30 pm"
)

fun getTestSpecificGoalEntity() = SpecificGoalsEntity(
    bookCountGoal = 5,
    goalId = "test123SGOAL",
    booksReadCount = 2,
    isActive = true
)

fun getTestBookGoalEntity() = BookGoalsEntity(
    bookId = "abab12345",
    isChapterGoal = true,
    goalInfo = "Read 1 chapter every day",
    isTimeGoal = false,
    goalSet = "Read 1 chapter every day",
    goalPeriod = "Every day",
    specificDays = listOf(),
    isActive = true
)

fun getTestBookVolumeDto() = BookVolumeDto(
    kind = "books#volumes",
    totalItems = 1,
    items = listOf(getTestBookDto())
)

fun getTestBookDto() = BookDto(
    id = "123TestBook_id",
    etag = "FoBs6doFx44",
    selfLink = "https://www.googleapis.com/books/v1/volumes/GGMmnL4VeOoC",
    volumeInfo = getVolumeInfo()
)

fun getVolumeInfo() = VolumeInfo(
    title = "Some title",
    subtitle = "Sub title",
    authors =  listOf("Author1" , "Author2"),
    publisher = "Some publisher",
    published_date = "15/10/2007",
    description = "Some description of course",
    pageCount = 200,
    industry_identifiers = listOf(getIdentifiers()) ,
    print_type = "hard cover" ,
    categories = listOf("science fiction"),
    image_links = getImageLinks()
)

fun getIdentifiers() = Identifiers(
    type = "ISBN" ,
    identifier = "JSDDC893457"
)

fun getImageLinks() =  ImageLinks(
    small_thumbnail =  "http://books.google.com/books/content?id=sBNazwEACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api" ,
    thumbnail = "http://books.google.com/books/content?id=sBNazwEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"
)