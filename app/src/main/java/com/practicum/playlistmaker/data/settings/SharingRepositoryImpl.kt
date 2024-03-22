package com.practicum.playlistmaker.data.settings

import com.practicum.playlistmaker.domain.settings.SharingRepository

class SharingRepositoryImpl(
    private val externalNavigator: ExternalNavigator
): SharingRepository {

    override fun shareApp() { externalNavigator.shareLink() }
    override fun openTerms() { externalNavigator.openLink() }
    override fun openSupport() { externalNavigator.openEmail() }
}