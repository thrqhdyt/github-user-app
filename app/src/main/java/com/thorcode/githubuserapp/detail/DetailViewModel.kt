package com.thorcode.githubuserapp.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.thorcode.core.data.source.Resource
import dev.thorcode.core.data.source.remote.network.ApiResponse
import dev.thorcode.core.data.source.remote.response.ItemsItem
import dev.thorcode.core.domain.model.DetailUser
import dev.thorcode.core.domain.model.User
import dev.thorcode.core.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val userUseCase: UserUseCase) : ViewModel() {
    private val _detailUser = MutableLiveData<Resource<DetailUser>>()
    val detailUser: LiveData<Resource<DetailUser>> = _detailUser

    private val _followers = MutableLiveData<ApiResponse<List<ItemsItem>>>()
    val followers: LiveData<ApiResponse<List<ItemsItem>>> = _followers

    private val _followings = MutableLiveData<ApiResponse<List<ItemsItem>>>()
    val followings: LiveData<ApiResponse<List<ItemsItem>>> = _followings

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            userUseCase.getDetailUser(username).collect {
                _detailUser.value = it
            }
        }
    }

    fun setFavoriteUser(user: User, newStatus: Boolean) =
        userUseCase.setFavoriteUser(user, newStatus)

     fun getListFollowers(username: String) {
         viewModelScope.launch {
             userUseCase.getListFollowers(username).collect {
                 _followers.value = it
             }
         }
    }

    fun getListFollowing(username: String) {
        viewModelScope.launch {
            userUseCase.getListFollowing(username).collect{
                _followings.value = it
            }
        }
    }
}