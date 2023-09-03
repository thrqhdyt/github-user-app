package dev.thorcode.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dev.thorcode.core.domain.usecase.UserUseCase

class FavoriteViewModel(userUseCase: UserUseCase) : ViewModel() {
    val favoriteUser = userUseCase.getFavoriteUser().asLiveData()
}