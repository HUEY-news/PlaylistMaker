package com.practicum.playlistmaker.domain.settings.impl

import com.practicum.playlistmaker.domain.settings.api.SharingInteractor
import com.practicum.playlistmaker.domain.settings.repository.SharingRepository

class SharingInteractorImpl(
    private val repository: SharingRepository
): SharingInteractor {

    override fun shareApp() { repository.shareApp() }
    override fun openTerms() { repository.openTerms() }
    override fun openSupport() { repository.openSupport() }
}