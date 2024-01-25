package com.practicum.playlistmaker.data.settings.api

interface SettingsLocalStorage {
    fun getThemeSettings(): Boolean
    fun updateThemeSettings(isChecked: Boolean)
}