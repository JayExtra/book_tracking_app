package com.dev.james.booktracker.core.user_preferences.mappers

import com.dev.james.booktracker.core.common_models.UserDetails
import com.dev.james.booktracker.core.entities.UserDetailsEntity

fun UserDetailsEntity.toDomain() : UserDetails {
    return UserDetails(
        username = username ,
        favouriteGenres = favouriteGenres ,
        selectedAvatar = selectedAvatar
    )
}