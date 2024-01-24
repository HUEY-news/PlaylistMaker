package com.practicum.playlistmaker.data.settings.repository

import com.practicum.playlistmaker.data.settings.api.SettingsLocalStorage
import com.practicum.playlistmaker.domain.settings.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val settingsLocalStorage: SettingsLocalStorage
): SettingsRepository {
    override fun getThemeState(): Boolean = settingsLocalStorage.getThemeSettings()
    override fun updateThemeState(isChecked: Boolean) = settingsLocalStorage.updateThemeSettings(isChecked)


}