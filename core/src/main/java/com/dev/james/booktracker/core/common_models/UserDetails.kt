package com.dev.james.booktracker.core.common_models

data class UserDetails(
    val username : String ,
    val favouriteGenres : List<String> ,
    val selectedAvatar : Int
)
