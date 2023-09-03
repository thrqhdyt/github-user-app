package dev.thorcode.core.utils

import dev.thorcode.core.data.source.local.entity.FavoriteUser
import dev.thorcode.core.data.source.remote.response.ItemsItem
import dev.thorcode.core.domain.model.User

object DataMapper {
    fun mapResponsesToEntities(input: List<ItemsItem>) : List<FavoriteUser> {
        val userList = ArrayList<FavoriteUser>()
        input.map {
            val user = FavoriteUser(
                id = it.id,
                username = it.login,
                avatarUrl = it.avatarUrl,
                isFavorite = false
            )
            userList.add(user)
        }
        return userList
    }

    fun mapEntitiesToDomain(input: List<FavoriteUser>): List<User> =
        input.map {
            User(
                id = it.id,
                username = it.username,
                avatarUrl = it.avatarUrl,
                isFavorite = it.isFavorite
            )
        }

    fun mapDomainToEntity(input: User) = FavoriteUser(
        id = input.id,
        username = input.username,
        avatarUrl = input.avatarUrl,
        isFavorite = input.isFavorite
    )
}