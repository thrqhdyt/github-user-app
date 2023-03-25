package com.thorcode.githubuserapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    var username: String = "",
    var avatarUrl: String? = null,
    var isFavorite: Boolean = false
)
