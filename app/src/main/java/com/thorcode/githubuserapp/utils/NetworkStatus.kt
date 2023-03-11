package com.thorcode.githubuserapp.utils

sealed class NetworkStatus {
    object SUCCESS : NetworkStatus()
    object LOADING : NetworkStatus()
    class FAILED(val message: String): NetworkStatus()
}