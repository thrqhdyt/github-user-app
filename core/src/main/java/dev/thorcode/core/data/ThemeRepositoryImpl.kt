package dev.thorcode.core.data

import dev.thorcode.core.data.source.local.SettingPreferences
import dev.thorcode.core.domain.repository.IThemeRepository
import kotlinx.coroutines.flow.Flow

class ThemeRepositoryImpl(private val preferences: SettingPreferences) : IThemeRepository {
    override fun getThemeSetting(): Flow<Boolean> =
        preferences.getThemeSetting()

    override suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        preferences.saveThemeSetting(isDarkModeActive)
}