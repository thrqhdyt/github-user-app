package com.thorcode.githubuserapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM user")
    fun getUserFav(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM user where isFavorite = 1")
    fun getIsFavorite(): LiveData<List<FavoriteUser>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToFav(favoriteUser: List<FavoriteUser>)

    @Update
    suspend fun updateUser(user: FavoriteUser)

    @Insert
    suspend fun insertUserFav(user: FavoriteUser)

    @Query("DELETE FROM user where isFavorite = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id AND isFavorite = 1)")
    fun isFavoriteUser(id: Int): LiveData<Boolean>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id)")
    suspend fun isExistUser(id: Int): Boolean
}