package com.dev.james.booktracker.core.user_preferences.data.mappers

import com.dev.james.booktracker.core.user_preferences.data.models.UserDetails
import com.dev.james.booktracker.core_database.room.entities.UserDetailsEntity

fun UserDetailsEntity.toDomain() : UserDetails {
    return UserDetails(
        username = username ,
        favouriteGenres = favouriteGenres ,
        selectedAvatar = selectedAvatar
    )
}