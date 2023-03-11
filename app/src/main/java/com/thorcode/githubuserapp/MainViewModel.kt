package com.thorcode.githubuserapp

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thorcode.githubuserapp.api.ApiConfig
import com.thorcode.githubuserapp.utils.NetworkStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _status = MutableLiveData<NetworkStatus>()
    val status: LiveData<NetworkStatus> = _status

    companion object {
        const val QUERY_TEMP = "thoriq"
    }

    init {
        getListUser()
    }

    fun getListUser(query: String = QUERY_TEMP) {
        _status.value = NetworkStatus.LOADING
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserGithub(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                    _status.value = NetworkStatus.SUCCESS
                } else {
                    Log.e("Failure", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("Failure", "onFailure: ${t.message.toString()}")
                _status.value = NetworkStatus.FAILED("you don't have internet access")
            }

        })
    }
}