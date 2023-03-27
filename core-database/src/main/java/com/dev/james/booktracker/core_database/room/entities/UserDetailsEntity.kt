package com.dev.james.booktracker.core_database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_details")
data class UserDetailsEntity(
    @ColumnInfo(name = "username")
    @PrimaryKey(autoGenerate = false)
    val username : String ,
    @ColumnInfo(name = "favourite_genres")
    val favouriteGenres : List<String> ,
    @ColumnInfo(name = "selected_avatar")
    val selectedAvatar : Int
)
