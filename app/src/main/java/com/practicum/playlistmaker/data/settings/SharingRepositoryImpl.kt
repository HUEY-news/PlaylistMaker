package com.practicum.playlistmaker.data.settings

import com.practicum.playlistmaker.domain.settings.SharingRepository
import javax.inject.Inject

class SharingRepositoryImpl @Inject constructor(
    private val externalNavigator: ExternalNavigator
): SharingRepository {

    override fun shareApp() { externalNavigator.shareLink() }
    override fun openTerms() { externalNavigator.openLink() }
    override fun openSupport() { externalNavigator.openEmail() }
}
