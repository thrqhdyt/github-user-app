package com.thorcode.githubuserapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.thorcode.core.domain.usecase.UserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class MainViewModel(private val userUseCase: UserUseCase) : ViewModel() {
    private val _keyword = MutableStateFlow("")
    private val keyword get() = _keyword.asStateFlow()
    @OptIn(ExperimentalCoroutinesApi::class)
    val usersByUsername get() = keyword.filter { it.isNotBlank() }.distinctUntilChanged().flatMapLatest {
        userUseCase.getUserByUsername(it)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val user =
        userUseCase.getAllUser().stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun onKeywordChanged(keyword: String){
        _keyword.value = keyword
    }
}