package com.practicum.playlistmaker.domain.settings

class SettingsInteractorImpl(
    private val repository: SettingsRepository
): SettingsInteractor {
    override fun getThemeState(): Boolean = repository.getThemeState()
    override fun updateThemeState(isChecked: Boolean) = repository.updateThemeState(isChecked)
}