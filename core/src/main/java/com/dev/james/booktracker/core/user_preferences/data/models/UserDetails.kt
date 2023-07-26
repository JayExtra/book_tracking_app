package com.dev.james.booktracker.core.user_preferences.data.models

data class UserDetails(
    val username : String ,
    val favouriteGenres : List<String> ,
    val selectedAvatar : Int
)
