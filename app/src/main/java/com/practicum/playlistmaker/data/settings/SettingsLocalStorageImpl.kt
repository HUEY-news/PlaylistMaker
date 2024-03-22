package com.practicum.playlistmaker.data.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.practicum.playlistmaker.app.DARK_THEME_KEY

class SettingsLocalStorageImpl(
    private val prefs: SharedPreferences
) : SettingsLocalStorage {

    override fun getThemeSettings(): Boolean {
        return prefs.getBoolean(DARK_THEME_KEY, false)
    }

    override fun updateThemeSettings(isChecked: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        prefs.edit {
            putBoolean(DARK_THEME_KEY, isChecked)
        }
    }
}