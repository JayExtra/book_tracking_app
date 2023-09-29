package com.dev.james.booktracker.core.user_preferences.mappers

import com.dev.james.booktracker.core.entities.UserDetailsEntity
import com.dev.james.booktracker.core.common_models.UserDetails

fun UserDetails.toEntity() : UserDetailsEntity {
    return UserDetailsEntity(
        username = username ,
        favouriteGenres = favouriteGenres ,
        selectedAvatar = selectedAvatar
    )
}