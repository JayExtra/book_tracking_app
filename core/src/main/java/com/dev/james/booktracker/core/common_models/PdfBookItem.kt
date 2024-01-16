package com.dev.james.booktracker.core.common_models

import android.net.Uri

data class PdfBookItem(
    val image : String = "" ,
    val author : String = "" ,
    val pages : Int = 0 ,
    val title : String = "" ,
    val publisher : String = "" ,
    val bookUri : Uri = Uri.EMPTY ,
    val date : String = ""
)
