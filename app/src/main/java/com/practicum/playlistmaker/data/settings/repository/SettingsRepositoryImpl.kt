package com.practicum.playlistmaker.data.settings.repository

import com.practicum.playlistmaker.data.settings.api.LocalStorage
import com.practicum.playlistmaker.domain.settings.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val localStorage: LocalStorage
): SettingsRepository {
    override fun getThemeState(): Boolean = localStorage.getThemeSettings()
    override fun updateThemeState(isChecked: Boolean) = localStorage.updateThemeSettings(isChecked)


}