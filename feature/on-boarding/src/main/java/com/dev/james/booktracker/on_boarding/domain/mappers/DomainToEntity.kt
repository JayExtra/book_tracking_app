package com.dev.james.booktracker.on_boarding.domain.mappers

import com.dev.james.booktracker.core_database.room.entities.UserDetailsEntity
import com.dev.james.booktracker.on_boarding.domain.models.UserDetails

fun UserDetails.toEntity() : UserDetailsEntity {
    return UserDetailsEntity(
        username = username ,
        favouriteGenres = favouriteGenres ,
        selectedAvatar = selectedAvatar
    )
}