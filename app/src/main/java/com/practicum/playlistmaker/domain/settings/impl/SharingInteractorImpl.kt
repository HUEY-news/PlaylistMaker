package com.practicum.playlistmaker.domain.settings.impl

import com.practicum.playlistmaker.domain.settings.api.SharingInteractor
import com.practicum.playlistmaker.domain.settings.repository.SharingRepository

class SharingInteractorImpl(
    private val sharingRepository: SharingRepository
): SharingInteractor {

    override fun shareApp() { sharingRepository.shareApp() }
    override fun openTerms() { sharingRepository.openTerms() }
    override fun openSupport() { sharingRepository.openSupport() }
}