package dev.thorcode.core.data

import dev.thorcode.core.data.source.NetworkBoundResource
import dev.thorcode.core.data.source.Resource
import dev.thorcode.core.data.source.local.LocalDataSource
import dev.thorcode.core.data.source.remote.RemoteDataSource
import dev.thorcode.core.data.source.remote.network.ApiResponse
import dev.thorcode.core.data.source.remote.response.ItemsItem
import dev.thorcode.core.domain.model.DetailUser
import dev.thorcode.core.domain.model.User
import dev.thorcode.core.domain.repository.IUserRepository
import dev.thorcode.core.utils.AppExecutors
import dev.thorcode.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IUserRepository {


    override fun getAllUser(): Flow<Resource<List<User>>> =
        object : NetworkBoundResource<List<User>, List<ItemsItem>>(){
            override fun loadFromDB(): Flow<List<User>> {
                return localDataSource.getAllUser().map { DataMapper.mapEntitiesToDomain(it) }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ItemsItem>>> =
                remoteDataSource.getAllUser()

            override suspend fun saveCallResult(data: List<ItemsItem>) {
                val userList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertUser(userList)
            }

            override fun shouldFetch(data: List<User>?): Boolean =
                data.isNullOrEmpty()
        }.asFlow()

    override suspend fun getUserByUsername(username: String): Flow<ApiResponse<List<User>>> =
        remoteDataSource.getUserByUsername(username)

    override fun getFavoriteUser(): Flow<List<User>> {
        return localDataSource.getFavoriteUser().map { DataMapper.mapEntitiesToDomain(it) }
    }

    override fun setFavoriteUser(user: User, state: Boolean) {
        val userEntity = DataMapper.mapDomainToEntity(user)
        appExecutors.diskIO().execute { localDataSource.setFavoriteUser(userEntity, state)}
    }

    override fun getDetailUser(username: String): Flow<Resource<DetailUser>> {
        return remoteDataSource.getDetailUser(username).map { apiResponse ->
            when (apiResponse) {
                is ApiResponse.Success -> {
                    val detailUserResponse = apiResponse.data
                    val detailUser = DetailUser(
                        id = detailUserResponse.id,
                        username = detailUserResponse.login,
                        name = detailUserResponse.name,
                        avatarUrl = detailUserResponse.avatarUrl,
                        following = detailUserResponse.following,
                        followers = detailUserResponse.followers,
                        isFavorite = false
                    )
                    Resource.Success(detailUser)
                }

                is ApiResponse.Error -> {
                    Resource.Error(apiResponse.errorMessage)
                }

                is ApiResponse.Empty -> {
                    Resource.Error("Empty Response")
                }

                else -> {
                    Resource.Loading()
                }
            }
        }
    }

    override fun getListFollowers(username: String): Flow<ApiResponse<List<ItemsItem>>> {
        return remoteDataSource.getListFollowers(username)
    }

    override fun getListFollowing(username: String): Flow<ApiResponse<List<ItemsItem>>> {
        return remoteDataSource.getListFollowing(username)
    }
}