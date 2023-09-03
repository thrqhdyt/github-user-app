package dev.thorcode.core.domain.usecase

import kotlinx.coroutines.flow.Flow

interface ThemeUseCase {
    fun getThemeSetting(): Flow<Boolean>

    suspend fun saveThemeSetting(isDarkModeActive: Boolean)
}