package dev.thorcode.core.data.source.remote

import dev.thorcode.core.data.source.remote.network.ApiResponse
import dev.thorcode.core.data.source.remote.network.ApiService
import dev.thorcode.core.data.source.remote.response.DetailGithubResponse
import dev.thorcode.core.data.source.remote.response.ItemsItem
import dev.thorcode.core.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService){
     suspend fun getAllUser(): Flow<ApiResponse<List<ItemsItem>>> {
         return flow {
             try {
                 val response = apiService.getUserGithub("thor")
                 val dataArray = response.items
                 if (dataArray.isNotEmpty()){
                     emit(ApiResponse.Success(response.items))
                 } else {
                     emit(ApiResponse.Empty)
                 }
             } catch (e: Exception){
                 emit(ApiResponse.Error(e.toString()))
             }
         }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserByUsername(username: String): Flow<ApiResponse<List<User>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = apiService.getUserGithub(username)
                val dataArray = response.items
                if (dataArray.isNotEmpty()){
                    emit(ApiResponse.Success(response.items.map {
                        User(id = it.id, username = it.login, avatarUrl = it.avatarUrl)
                    }))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getDetailUser(username: String): Flow<ApiResponse<DetailGithubResponse>> {
        return flow {
            try {
                val response: DetailGithubResponse = apiService.getDetailUser(username)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getListFollowers(username: String): Flow<ApiResponse<List<ItemsItem>>> {
        return flow {
            try {
                val response = apiService.getFollowers(username)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getListFollowing(username: String): Flow<ApiResponse<List<ItemsItem>>> {
        return flow {
            try {
                val response = apiService.getFollowing(username)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}