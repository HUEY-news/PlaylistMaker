package com.practicum.playlistmaker.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.SharingInteractor
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInterractor: SharingInteractor
): ViewModel() {

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
}