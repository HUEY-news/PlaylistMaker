package com.practicum.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val PREFERENCES_FOLDER_NAME = "PREFERENCES"
const val DARK_THEME_KEY = "DARK_THEME_ENABLED"

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(viewModelModule, dataModule, interactorModule, repositoryModule)
        }

        // реализация загрузки темы:
        val sharedPreferences = getSharedPreferences(PREFERENCES_FOLDER_NAME, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, darkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}