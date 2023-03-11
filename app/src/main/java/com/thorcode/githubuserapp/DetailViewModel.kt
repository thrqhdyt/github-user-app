package com.thorcode.githubuserapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thorcode.githubuserapp.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _detailUser = MutableLiveData<DetailGithubResponse>()
    val detailUser: LiveData<DetailGithubResponse> = _detailUser

    private val _listFollows = MutableLiveData<List<ItemsItem>>()
    val listFollows: LiveData<List<ItemsItem>> = _listFollows

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetailUser(username: String?) {
        _isLoading.value = true
        username?.let {
            val client = ApiConfig.getApiService().getDetailUser(it)
            client.enqueue(object: Callback<DetailGithubResponse>{
                override fun onResponse(
                    call: Call<DetailGithubResponse>,
                    response: Response<DetailGithubResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        _detailUser.value = response.body()
                    }
                }

                override fun onFailure(call: Call<DetailGithubResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    fun getFollowers(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollows.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getFollowings(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollows.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}