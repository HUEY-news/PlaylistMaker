package com.practicum.playlistmaker.data.settings

import com.practicum.playlistmaker.domain.settings.SettingsRepository

class SettingsRepositoryImpl(
    private val storage: SettingsLocalStorage
): SettingsRepository {
    override fun getThemeState(): Boolean = storage.getThemeSettings()
    override fun updateThemeState(isChecked: Boolean) = storage.updateThemeSettings(isChecked)


}