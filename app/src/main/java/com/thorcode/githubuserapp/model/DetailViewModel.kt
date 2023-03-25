package com.thorcode.githubuserapp.model

import androidx.lifecycle.*
import com.thorcode.githubuserapp.repository.UserRepository
import com.thorcode.githubuserapp.api.DetailGithubResponse
import com.thorcode.githubuserapp.database.FavoriteUser
import com.thorcode.githubuserapp.utils.NetworkStatus
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {
    private val _detailUser = MutableLiveData<DetailGithubResponse>()
    val detailUser: LiveData<DetailGithubResponse> = _detailUser

    private val _listFollows = MutableLiveData<List<FavoriteUser>>()
    val listFollows: LiveData<List<FavoriteUser>> = _listFollows

    private val _isLoading = MutableLiveData<Boolean>()

    private val _status = MutableLiveData<NetworkStatus>()
    val status: LiveData<NetworkStatus> = _status

    fun getDetailUser(username: String?) = viewModelScope.launch {
        _status.value = NetworkStatus.LOADING
        _isLoading.value = true
        try {
            username?.let {
                _detailUser.value = repository.getDetailUser(it)
                _status.value = NetworkStatus.SUCCESS
            }
        } catch (e: Exception){
            _status.value = NetworkStatus.FAILED("Please check your connection!!")
        }
    }

    fun getFollowers(username: String) = viewModelScope.launch {
        _status.value = NetworkStatus.LOADING
        _isLoading.value = true

        try {
            username.let {
                _listFollows.value = repository.getFollowers(it)
                _status.value = NetworkStatus.SUCCESS
            }
        } catch (e: Exception){
            _status.value = NetworkStatus.FAILED("Please check your connection!!")
        }
    }

    fun getFollowings(username: String) = viewModelScope.launch{
        _status.value = NetworkStatus.LOADING
        _isLoading.value = true
        try {
            username.let {
                _listFollows.value = repository.getFollowing(it)
                _status.value = NetworkStatus.SUCCESS
            }
        } catch (e: Exception){
            _status.value = NetworkStatus.FAILED("Please check your connection!!")
        }
    }

    fun isFavoritedUser(id: Int) = repository.isFavoritedUser(id)

    fun saveUser(user: DetailGithubResponse, isFavorite: Boolean) = viewModelScope.launch {
        repository.setIsFavoriteUser(user, isFavorite)
    }

    class Factory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown MainViewModel class: " + modelClass.name)
        }
    }
}