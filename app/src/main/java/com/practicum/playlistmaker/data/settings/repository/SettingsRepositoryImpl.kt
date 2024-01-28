package com.practicum.playlistmaker.data.settings.repository

import com.practicum.playlistmaker.data.settings.api.SettingsLocalStorage
import com.practicum.playlistmaker.domain.settings.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val storage: SettingsLocalStorage
): SettingsRepository {
    override fun getThemeState(): Boolean = storage.getThemeSettings()
    override fun updateThemeState(isChecked: Boolean) = storage.updateThemeSettings(isChecked)


}