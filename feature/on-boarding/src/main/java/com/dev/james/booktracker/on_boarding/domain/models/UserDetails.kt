package com.dev.james.booktracker.on_boarding.domain.models

data class UserDetails(
    val username : String ,
    val favouriteGenres : List<String> ,
    val selectedAvatar : Int
)
