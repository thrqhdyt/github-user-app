package com.thorcode.githubuserapp.di

import com.thorcode.githubuserapp.detail.DetailViewModel
import com.thorcode.githubuserapp.home.MainViewModel
import com.thorcode.githubuserapp.theme.ThemeViewModel
import dev.thorcode.core.domain.usecase.ThemeInteractor
import dev.thorcode.core.domain.usecase.ThemeUseCase
import dev.thorcode.core.domain.usecase.UserInteractor
import dev.thorcode.core.domain.usecase.UserUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<UserUseCase> { UserInteractor(get()) }
    factory<ThemeUseCase> { ThemeInteractor(get())  }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { ThemeViewModel(get()) }
}