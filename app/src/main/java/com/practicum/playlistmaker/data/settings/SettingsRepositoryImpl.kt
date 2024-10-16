package com.practicum.playlistmaker.data.settings

import com.practicum.playlistmaker.domain.settings.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val storage: SettingsLocalStorage
): SettingsRepository {
    override fun getThemeState(): Boolean = storage.getThemeSettings()
    override fun updateThemeState(isChecked: Boolean) = storage.updateThemeSettings(isChecked)
}
