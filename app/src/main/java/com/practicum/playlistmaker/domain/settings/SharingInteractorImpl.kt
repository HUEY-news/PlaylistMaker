package com.practicum.playlistmaker.domain.settings

class SharingInteractorImpl(
    private val repository: SharingRepository
): SharingInteractor {

    override fun shareApp() { repository.shareApp() }
    override fun openTerms() { repository.openTerms() }
    override fun openSupport() { repository.openSupport() }
}