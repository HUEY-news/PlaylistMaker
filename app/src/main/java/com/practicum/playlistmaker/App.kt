package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

const val PREFERENCES_FOLDER_NAME = "PREFERENCES"
const val DARK_THEME_KEY = "DARK_THEME_ENABLED"

class App : Application() {
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        init(this)

        // реализация загрузки темы:
        darkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, darkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    // реализация переключения темы:
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        // реализация сохранения темы:
        sharedPreferences.edit {
            putBoolean(DARK_THEME_KEY, darkTheme)
        }
    }

    companion object PreferencesProvider{
        lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context){
            sharedPreferences = context.getSharedPreferences(PREFERENCES_FOLDER_NAME, MODE_PRIVATE)
        }
    }
}