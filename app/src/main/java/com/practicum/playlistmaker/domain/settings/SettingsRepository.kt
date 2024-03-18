package com.practicum.playlistmaker.domain.settings

interface SettingsRepository {
    fun getThemeState(): Boolean
    fun updateThemeState(isChecked: Boolean)
}