package dev.thorcode.core.data.source.local

import dev.thorcode.core.data.source.local.entity.FavoriteUser
import dev.thorcode.core.data.source.local.room.FavoriteUserDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val userDao: FavoriteUserDao){

    fun getAllUser(): Flow<List<FavoriteUser>> = userDao.getUserFav()

    fun getFavoriteUser(): Flow<List<FavoriteUser>> = userDao.getIsFavorite()

    suspend fun insertUser(userList: List<FavoriteUser>) = userDao.insertToFav(userList)

    fun setFavoriteUser(user: FavoriteUser, newState: Boolean) {
        user.isFavorite = newState
        userDao.updateUser(user)
    }
}