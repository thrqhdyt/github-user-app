package dev.thorcode.core.domain.usecase

import dev.thorcode.core.domain.repository.IThemeRepository
import kotlinx.coroutines.flow.Flow

class ThemeInteractor(private val repository: IThemeRepository): ThemeUseCase {
    override fun getThemeSetting(): Flow<Boolean> =
        repository.getThemeSetting()

    override suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        repository.saveThemeSetting(isDarkModeActive)
}