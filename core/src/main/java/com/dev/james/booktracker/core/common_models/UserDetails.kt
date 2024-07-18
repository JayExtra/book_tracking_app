package com.dev.james.booktracker.core.common_models

data class UserDetails(
    val username : String = "Fetching username..." ,
    val favouriteGenres : List<String> = listOf() ,
    val selectedAvatar : Int = 0
)
