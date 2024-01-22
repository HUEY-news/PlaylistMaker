package com.practicum.playlistmaker.domain.settings.impl

import com.practicum.playlistmaker.domain.settings.api.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.repository.SettingsRepository

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
): SettingsInteractor {
    override fun getThemeState(): Boolean = settingsRepository.getThemeState()
    override fun updateThemeState(isChecked: Boolean) = settingsRepository.updateThemeState(isChecked)
}