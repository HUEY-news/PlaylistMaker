package com.practicum.playlistmaker.domain.settings.repository

interface SettingsRepository {
    fun getThemeState(): Boolean
    fun updateThemeState(isChecked: Boolean)
}