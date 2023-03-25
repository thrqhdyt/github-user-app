package com.thorcode.githubuserapp.api

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