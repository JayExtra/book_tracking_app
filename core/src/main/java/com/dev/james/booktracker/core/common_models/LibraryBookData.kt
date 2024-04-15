package com.dev.james.booktracker.core.common_models

data class LibraryBookData(
    val baseData : BaseBookData = BaseBookData() ,
    val progress: Int = 0 ,
    val pagesRead : Int = 0 ,
    val isPending : Boolean = false
)
