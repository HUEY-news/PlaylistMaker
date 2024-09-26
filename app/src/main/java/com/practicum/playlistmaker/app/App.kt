package com.practicum.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

const val PREFERENCES_FOLDER_NAME = "PREFERENCES"
const val DARK_THEME_KEY = "DARK_THEME_ENABLED"

@HiltAndroidApp
class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        // реализация загрузки темы:
        val sharedPreferences = getSharedPreferences(PREFERENCES_FOLDER_NAME, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, darkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}