package com.practicum.playlistmaker.data.settings

interface SettingsLocalStorage {
    fun getThemeSettings(): Boolean
    fun updateThemeSettings(isChecked: Boolean)
}