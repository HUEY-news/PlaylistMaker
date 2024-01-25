package com.practicum.playlistmaker.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.practicum.playlistmaker.di.appModule
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

const val PREFERENCES_FOLDER_NAME = "PREFERENCES"
const val DARK_THEME_KEY = "DARK_THEME_ENABLED"

class App : Application() {
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(appModule, dataModule, domainModule)
        }

        init(this)

        // реализация загрузки темы:
        darkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, darkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun switchThemeState(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        sharedPreferences.edit {
            putBoolean(DARK_THEME_KEY, darkTheme)
        }
    }

    fun getThemeState() = sharedPreferences.getBoolean(DARK_THEME_KEY, darkTheme)

    companion object PreferencesProvider{
        lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context){
            sharedPreferences = context.getSharedPreferences(PREFERENCES_FOLDER_NAME, MODE_PRIVATE)
        }
    }
}