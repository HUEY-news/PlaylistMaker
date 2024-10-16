package com.practicum.playlistmaker.data.settings

import com.practicum.playlistmaker.domain.settings.SharingRepository
import javax.inject.Inject

class SharingRepositoryImpl @Inject constructor(
    private val navigator: ExternalNavigator
): SharingRepository {

    override fun shareApp() { navigator.shareLink() }
    override fun openTerms() { navigator.openLink() }
    override fun openSupport() { navigator.openEmail() }
}
