package com.thorcode.githubuserapp.model

import androidx.lifecycle.*
import com.thorcode.githubuserapp.preferences.SettingPreferences
import com.thorcode.githubuserapp.repository.UserRepository
import com.thorcode.githubuserapp.database.FavoriteUser
import com.thorcode.githubuserapp.utils.NetworkStatus
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: SettingPreferences, private val repository: UserRepository) : ViewModel() {

    private val _listUser = MutableLiveData<List<FavoriteUser>>()
    val listUser: LiveData<List<FavoriteUser>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()

    private val _status = MutableLiveData<NetworkStatus>()

    init {
        getListUser()
    }

    fun getListUser(query: String = QUERY_TEMP) = repository.getUsers(query)

    fun getListUserByUsername(username: String) = viewModelScope.launch{
        _status.value = NetworkStatus.LOADING
        _isLoading.value = true
        try {
            username.let {
                _listUser.value = repository.getUsersByUsername(it)
                _status.value = NetworkStatus.SUCCESS
            }
        } catch (e: Exception){
            _status.value = NetworkStatus.FAILED("Please check your connection!!")
        }
    }

    fun getTheme() = preferences.getThemeSetting().asLiveData()

    class Factory(private val pref: SettingPreferences, private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(pref, userRepository) as T
            }
            throw IllegalArgumentException("Unknown MainViewModel class: " + modelClass.name)
        }
    }

    companion object {
        const val QUERY_TEMP = "thor"
    }
}