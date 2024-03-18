package com.practicum.playlistmaker.domain.settings

interface SettingsInteractor {
    fun getThemeState(): Boolean
    fun updateThemeState(isChecked: Boolean)
}