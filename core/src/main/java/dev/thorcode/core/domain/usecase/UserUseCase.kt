package dev.thorcode.core.domain.usecase

import dev.thorcode.core.data.source.Resource
import dev.thorcode.core.data.source.remote.network.ApiResponse
import dev.thorcode.core.data.source.remote.response.ItemsItem
import dev.thorcode.core.domain.model.DetailUser
import dev.thorcode.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserUseCase {

    fun getAllUser(): Flow<Resource<List<User>>>

    suspend fun getUserByUsername(username: String): Flow<ApiResponse<List<User>>>

    fun getFavoriteUser(): Flow<List<User>>

    fun setFavoriteUser(user: User, state: Boolean)

    fun getDetailUser(username: String): Flow<Resource<DetailUser>>

    fun getListFollowers(username: String): Flow<ApiResponse<List<ItemsItem>>>

    fun getListFollowing(username: String): Flow<ApiResponse<List<ItemsItem>>>
}