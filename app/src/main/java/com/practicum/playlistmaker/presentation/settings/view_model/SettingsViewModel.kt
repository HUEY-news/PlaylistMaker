package com.practicum.playlistmaker.presentation.settings.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.settings.api.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.api.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInterractor: SharingInteractor
): ViewModel() {
    init { Log.v("TEST", "SettingsViewModel СОЗДАНА") }

    private var themeStateLiveData = MutableLiveData(getThemeState())
    fun getThemeStateLiveData(): LiveData<Boolean> = themeStateLiveData

    private fun getThemeState(): Boolean = settingsInteractor.getThemeState()

    fun updateThemeState(isChecked: Boolean) {
        settingsInteractor.updateThemeState(isChecked)
        themeStateLiveData.postValue(getThemeState())
    }

    fun shareApp() = sharingInterractor.shareApp()
    fun openTerms() = sharingInterractor.openTerms()
    fun openSupport() = sharingInterractor.openSupport()

    override fun onCleared() { Log.v("TEST", "SettingsViewModel ОЧИЩЕНА") }
}