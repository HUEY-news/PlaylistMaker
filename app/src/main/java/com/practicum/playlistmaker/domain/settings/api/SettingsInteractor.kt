package com.practicum.playlistmaker.domain.settings.api

interface SettingsInteractor {
    fun getThemeState(): Boolean
    fun updateThemeState(isChecked: Boolean)
}