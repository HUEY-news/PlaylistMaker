package com.practicum.playlistmaker.domain.settings

import javax.inject.Inject

class SettingsInteractorImpl @Inject constructor(
    private val repository: SettingsRepository
): SettingsInteractor {
    override fun getThemeState(): Boolean = repository.getThemeState()
    override fun updateThemeState(isChecked: Boolean) = repository.updateThemeState(isChecked)
}
