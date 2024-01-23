package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.practicum.playlistmaker.app.App
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.player.api.Player
import com.practicum.playlistmaker.data.player.impl.PlayerImpl
import com.practicum.playlistmaker.data.player.repository.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.SearchHistory
import com.practicum.playlistmaker.data.settings.api.ExternalNavigator
import com.practicum.playlistmaker.data.settings.api.LocalStorage
import com.practicum.playlistmaker.data.settings.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.data.settings.impl.LocalStorageImpl
import com.practicum.playlistmaker.data.settings.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.settings.repository.SharingRepositoryImpl
import com.practicum.playlistmaker.data.track.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.player.repository.PlayerRepository
import com.practicum.playlistmaker.domain.settings.api.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.api.SharingInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.settings.impl.SharingInteractorImpl
import com.practicum.playlistmaker.domain.settings.repository.SettingsRepository
import com.practicum.playlistmaker.domain.settings.repository.SharingRepository
import com.practicum.playlistmaker.domain.track.TrackInteractor
import com.practicum.playlistmaker.domain.track.TrackInteractorImpl
import com.practicum.playlistmaker.domain.track.TrackRepository

object Creator {

    fun provideSearchHistory() = SearchHistory(getSharedPreferences())
    private fun getSharedPreferences() = App.sharedPreferences

    fun provideTrackInteractor(context: Context): TrackInteractor = TrackInteractorImpl(getTrackRepository(context))
    private fun getTrackRepository(context: Context): TrackRepository = TrackRepositoryImpl(getNetworkClient(context))
    private fun getNetworkClient(context: Context): NetworkClient = RetrofitNetworkClient(context)

    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorImpl(playerRepository = getPlayerRepository())
    private fun getPlayerRepository(): PlayerRepository = PlayerRepositoryImpl(player = getPlayer())
    private fun getPlayer(): Player = PlayerImpl(MediaPlayer())

    fun provideSharingInteractor(context: Context): SharingInteractor = SharingInteractorImpl(getSharedRepository(context))
    private fun getSharedRepository(context: Context): SharingRepository = SharingRepositoryImpl(getExternalNavigator(context))
    private fun getExternalNavigator(context: Context): ExternalNavigator = ExternalNavigatorImpl(context)

    fun provideSettingsInteractor(application: Application): SettingsInteractor = SettingsInteractorImpl(getSettingsRepository(application))
    private fun getSettingsRepository(application: Application): SettingsRepository = SettingsRepositoryImpl(getLocalStorage(application))
    private fun getLocalStorage(application: Application): LocalStorage = LocalStorageImpl(application)

}