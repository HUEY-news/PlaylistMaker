package com.practicum.playlistmaker.domain.settings.impl

import com.practicum.playlistmaker.domain.settings.api.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.repository.SettingsRepository

class SettingsInteractorImpl(
    private val repository: SettingsRepository
): SettingsInteractor {
    override fun getThemeState(): Boolean = repository.getThemeState()
    override fun updateThemeState(isChecked: Boolean) = repository.updateThemeState(isChecked)
}