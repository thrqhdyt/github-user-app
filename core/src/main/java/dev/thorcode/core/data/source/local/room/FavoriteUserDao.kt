package dev.thorcode.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.thorcode.core.data.source.local.entity.FavoriteUser
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM user")
    fun getUserFav(): Flow<List<FavoriteUser>>

    @Query("SELECT * FROM user where isFavorite = 1")
    fun getIsFavorite(): Flow<List<FavoriteUser>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToFav(favoriteUser: List<FavoriteUser>)

    @Update
    fun updateUser(user: FavoriteUser)

    @Insert
    fun insertUserFav(user: FavoriteUser)

    @Transaction
    fun upsertUser(user: FavoriteUser) {
        if (isExistUser(user.id)){
            updateUser(user)
        } else {
            insertUserFav(user)
        }
    }

    @Query("DELETE FROM user where isFavorite = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id AND isFavorite = 1)")
    fun isFavoriteUser(id: Int): LiveData<Boolean>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id)")
    fun isExistUser(id: Int): Boolean
}