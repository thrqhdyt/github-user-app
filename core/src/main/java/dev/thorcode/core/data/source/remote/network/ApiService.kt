package dev.thorcode.core.data.source.remote.network

import dev.thorcode.core.data.source.remote.response.DetailGithubResponse
import dev.thorcode.core.data.source.remote.response.GithubResponse
import dev.thorcode.core.data.source.remote.response.ItemsItem
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    suspend fun getUserGithub(
        @Query("q") query: String
    ): GithubResponse

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): DetailGithubResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): List<ItemsItem>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): List<ItemsItem>
}