package dev.thorcode.core.domain.usecase

import dev.thorcode.core.data.source.Resource
import dev.thorcode.core.data.source.remote.network.ApiResponse
import dev.thorcode.core.data.source.remote.response.ItemsItem
import dev.thorcode.core.domain.model.DetailUser
import dev.thorcode.core.domain.model.User
import dev.thorcode.core.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow

class UserInteractor(private val userRepository: IUserRepository) : UserUseCase {
    override fun getAllUser(): Flow<Resource<List<User>>> = userRepository.getAllUser()
    override suspend fun getUserByUsername(username: String): Flow<ApiResponse<List<User>>> =
        userRepository.getUserByUsername(username)

    override fun getFavoriteUser() = userRepository.getFavoriteUser()

    override fun setFavoriteUser(user: User, state: Boolean) = userRepository.setFavoriteUser(user, state)

    override fun getDetailUser(username: String): Flow<Resource<DetailUser>> = userRepository.getDetailUser(username)

    override fun getListFollowers(username: String): Flow<ApiResponse<List<ItemsItem>>> =
        userRepository.getListFollowers(username)

    override fun getListFollowing(username: String): Flow<ApiResponse<List<ItemsItem>>> =
        userRepository.getListFollowing(username)
}