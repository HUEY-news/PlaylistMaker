package com.practicum.playlistmaker.data.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.practicum.playlistmaker.app.DARK_THEME_KEY
import javax.inject.Inject

class SettingsLocalStorageImpl @Inject constructor(
    private val preferences: SharedPreferences
) : SettingsLocalStorage {

    override fun getThemeSettings(): Boolean {
        return preferences.getBoolean(DARK_THEME_KEY, false)
    }

    override fun updateThemeSettings(isChecked: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        preferences.edit {
            putBoolean(DARK_THEME_KEY, isChecked)
        }
    }
}
