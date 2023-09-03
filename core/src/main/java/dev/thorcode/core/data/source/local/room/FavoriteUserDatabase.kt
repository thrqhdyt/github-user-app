package dev.thorcode.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.thorcode.core.data.source.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1)
abstract class FavoriteUserDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao
}