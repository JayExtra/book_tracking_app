package com.dev.james.booktracker.core.common_models

data class ReadingLists(
    val id : String = "",
    val name : String = "",
    val image : String = "",
    val description : String = "",
    val readingList : List<LibraryBookData> = emptyList<LibraryBookData>(),
    val date : String = "",
    val starred : Boolean = false
)
