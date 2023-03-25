package com.thorcode.githubuserapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.thorcode.githubuserapp.api.ApiService
import com.thorcode.githubuserapp.api.DetailGithubResponse
import com.thorcode.githubuserapp.database.FavoriteUser
import com.thorcode.githubuserapp.database.FavoriteUserDao
import com.thorcode.githubuserapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository private constructor(
    private val apiService: ApiService,
    private val favoriteUserDao: FavoriteUserDao,
){
    suspend fun getUsersByUsername(username: String) =
        withContext(Dispatchers.IO){
            val userResponses = apiService.getUserGithub(username).items
            userResponses.map {
                FavoriteUser(
                    id = it.id,
                    username = it.login,
                    avatarUrl = it.avatarUrl,
                )
            }
        }

    fun getUsers(query: String): LiveData<Result<List<FavoriteUser>>> = liveData {
        withContext(Dispatchers.IO){
            emit(Result.Loading)
            try {
                val response = apiService.getUserGithub(query).items
                val users = response.map { user ->
                    val isFavoriteUser = favoriteUserDao.isFavoriteUser(user.id)
                    FavoriteUser(
                        id = user.id,
                        avatarUrl = user.avatarUrl,
                        username = user.login,
                        isFavorite = isFavoriteUser.value?: false
                    )
                }
                favoriteUserDao.deleteAll()
                favoriteUserDao.insertToFav(users)
            } catch (e: Exception) {
                Log.d("User Repository", "getUser: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
            val localData: LiveData<Result<List<FavoriteUser>>> = favoriteUserDao.getUserFav().map { Result.Success(it) }
            emitSource(localData)
        }
    }

    suspend fun getDetailUser(
        username: String
    ) = apiService.getDetailUser(username)

    suspend fun getFollowers(username: String):List<FavoriteUser> = withContext(Dispatchers.IO){
        val userResponses = apiService.getFollowers(username)
        userResponses.map {
            FavoriteUser(
                id = it.id,
                username = it.login,
                avatarUrl = it.avatarUrl,
            )
        }
    }

    suspend fun getFollowing(username: String):List<FavoriteUser> = withContext(Dispatchers.IO){
        val userResponses = apiService.getFollowing(username)
        userResponses.map {
            FavoriteUser(
                id = it.id,
                username = it.login,
                avatarUrl = it.avatarUrl,
            )
        }
    }

    fun isFavoritedUser(id: Int) = favoriteUserDao.isFavoriteUser(id)

    fun getIsFavoriteUser(): LiveData<List<FavoriteUser>> {
        return favoriteUserDao.getIsFavorite()
    }

    suspend fun setIsFavoriteUser(user: DetailGithubResponse, isFavState: Boolean) =
        withContext(Dispatchers.IO) {
            val isUserExist = favoriteUserDao.isExistUser(user.id)
            val favoriteUser = FavoriteUser(
                id = user.id,
                username = user.login,
                avatarUrl = user.avatarUrl,
                isFavorite = isFavState
            )
            if(!isUserExist){
                favoriteUserDao.insertUserFav(favoriteUser)
            }else{
                favoriteUserDao.updateUser(favoriteUser)
            }
        }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: FavoriteUserDao,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService = apiService, favoriteUserDao = userDao )
            }.also { instance = it }
    }
}