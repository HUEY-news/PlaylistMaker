package com.practicum.playlistmaker.data.settings.impl

import android.app.Application
import com.practicum.playlistmaker.app.App
import com.practicum.playlistmaker.data.settings.api.SettingsLocalStorage

class SettingsLocalStorageImpl(
    private val application: Application
) : SettingsLocalStorage {

    override fun getThemeSettings(): Boolean {
        return (application as App).getThemeState()
    }

    override fun updateThemeSettings(isChecked: Boolean) {
        (application as App).switchThemeState(isChecked)
    }
}