package com.dev.james.booktracker.core.test_commons

import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.entities.BookEntity
import com.dev.james.booktracker.core.entities.BookGoalsEntity
import com.dev.james.booktracker.core.entities.GoalEntity
import com.dev.james.booktracker.core.entities.SpecificGoalsEntity
import com.dev.james.booktracker.core.dto.BookDto
import com.dev.james.booktracker.core.dto.BookVolumeDto
import com.dev.james.booktracker.core.dto.Identifiers
import com.dev.james.booktracker.core.dto.ImageLinks
import com.dev.james.booktracker.core.dto.VolumeInfo

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

fun getTestOverallGoalEntity() = GoalEntity(
    id = "test1234GOAL",
    information = "Read for 10 minutes every day",
    time = 1600000L,
    period = "Every day",
    selectedDays = listOf(),
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

fun getFakeBookSaveWithUri() = BookSave(
    bookId = "74298ucwreovfjoifw11" ,
    bookImage = "app:booktracker/home/place/someimage.jpg" ,
    bookTitle = "some book title" ,
    bookAuthors = "smith , wesson" ,
    bookSmallThumbnail = "n/a",
    bookPagesCount = 122 ,
    publisher = "n/a" ,
    publishedDate = "n/a" ,
    isUri = true ,
    currentChapter = 14 ,
    currentChapterTitle = "some title" ,
    chapters = 22
)

fun getFakeBookSaveWithUrl() = BookSave(
    bookId = "33434343weffsdvsdvsdfjoifw11" ,
    bookImage = "https://booktracker.com/collections/images/someimages_2015.jpg" ,
    bookTitle = "some book title" ,
    bookAuthors = "smith , wesson" ,
    bookSmallThumbnail = "https://booktracker.com/collections/thumbnails/sometumbnail_2015.jpg",
    bookPagesCount = 122 ,
    publisher = "McMillan Publishers" ,
    publishedDate = "14/02/2005" ,
    isUri = false ,
    currentChapter = 12 ,
    currentChapterTitle = "some title 2" ,
    chapters = 3
)