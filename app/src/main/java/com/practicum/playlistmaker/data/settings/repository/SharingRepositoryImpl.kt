package com.practicum.playlistmaker.data.settings.repository

import com.practicum.playlistmaker.data.settings.api.ExternalNavigator
import com.practicum.playlistmaker.domain.settings.repository.SharingRepository

class SharingRepositoryImpl(
    private val externalNavigator: ExternalNavigator
): SharingRepository {

    override fun shareApp() { externalNavigator.shareLink() }
    override fun openTerms() { externalNavigator.openLink() }
    override fun openSupport() { externalNavigator.openEmail() }
}