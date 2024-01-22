package com.practicum.playlistmaker.data.settings.impl

import android.app.Application
import com.practicum.playlistmaker.app.App
import com.practicum.playlistmaker.data.settings.api.LocalStorage

class LocalStorageImpl(
    private val application: Application
) : LocalStorage {

    override fun getThemeSettings(): Boolean {
        return (application as App).getThemeState()
    }

    override fun updateThemeSettings(isChecked: Boolean) {
        (application as App).switchThemeState(isChecked)
    }
}