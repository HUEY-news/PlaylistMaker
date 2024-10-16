package com.practicum.playlistmaker.domain.settings

import javax.inject.Inject

class SharingInteractorImpl @Inject constructor(
    private val repository: SharingRepository
): SharingInteractor {

    override fun shareApp() { repository.shareApp() }
    override fun openTerms() { repository.openTerms() }
    override fun openSupport() { repository.openSupport() }
}
