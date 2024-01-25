package com.practicum.playlistmaker.presentation.settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.practicum.playlistmaker.creator.Creator


class SettingsViewModelFactory: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val application = checkNotNull(extras.get(APPLICATION_KEY))
        return SettingsViewModel(
            Creator.provideSharingInteractor(application),
            Creator.provideSettingsInteractor(application)
        ) as T
    }
}