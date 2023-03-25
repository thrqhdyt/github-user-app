package com.thorcode.githubuserapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thorcode.githubuserapp.repository.UserRepository

class FavoriteViewModel(private val repository: UserRepository) : ViewModel() {

    fun getListUserFav() = repository.getIsFavoriteUser()

    class Factory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                return FavoriteViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown FavoriteViewModel class: " + modelClass.name)
        }
    }
}