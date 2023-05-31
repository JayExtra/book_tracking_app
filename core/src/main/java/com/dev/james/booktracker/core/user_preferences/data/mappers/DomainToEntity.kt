package com.dev.james.booktracker.core.user_preferences.data.mappers

import com.dev.james.booktracker.core_database.room.entities.UserDetailsEntity
import com.dev.james.booktracker.core.user_preferences.data.models.UserDetails

fun UserDetails.toEntity() : UserDetailsEntity {
    return UserDetailsEntity(
        username = username ,
        favouriteGenres = favouriteGenres ,
        selectedAvatar = selectedAvatar
    )
}