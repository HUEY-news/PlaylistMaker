package com.practicum.playlistmaker.data.settings.api

interface LocalStorage {
    fun getThemeSettings(): Boolean
    fun updateThemeSettings(isChecked: Boolean)
}