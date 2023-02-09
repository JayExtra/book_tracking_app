package com.dev.james.booktracker.on_boarding.domain.mappers

import com.dev.james.booktracker.core_database.room.entities.UserDetailsEntity
import com.dev.james.booktracker.on_boarding.domain.models.UserDetails

fun UserDetailsEntity.toDomain() : UserDetails {
    return UserDetails(
        username = username ,
        favouriteGenres = favouriteGenres ,
        selectedAvatar = selectedAvatar
    )
}