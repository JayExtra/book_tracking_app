package com.dev.james.booktracker.home.presentation.states

data class AddReadFormState(
    val showPlaceHolderImage : Boolean = true ,
    val showImageProgress : Boolean = false ,
    val selectedImage : String = "" ,
    val titleText : String = "" ,
    val titleFieldError : String? = null ,
    val authorText : String = "" ,
    val authorFieldError : String? = null ,
    val selectedChapter : String = "" ,
    val selectedChapterFieldError : String? = null ,
    val currentChapterText : String = "" ,
    val currentChapterFieldError : String? = null ,
    val currentChapterTitle : String = "" ,
    val currentChapterTitleError : String? = null ,
)
