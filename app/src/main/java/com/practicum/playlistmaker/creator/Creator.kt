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
import com.practicum.playlistmaker.data.search.api.SearchLocalStorage
import com.practicum.playlistmaker.data.search.impl.SearchLocalStorageImpl
import com.practicum.playlistmaker.data.search.repository.SearchRepositoryImpl
import com.practicum.playlistmaker.data.settings.api.ExternalNavigator
import com.practicum.playlistmaker.data.settings.api.SettingsLocalStorage
import com.practicum.playlistmaker.data.settings.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.data.settings.impl.SettingsLocalStorageImpl
import com.practicum.playlistmaker.data.settings.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.settings.repository.SharingRepositoryImpl
import com.practicum.playlistmaker.data.track.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.player.repository.PlayerRepository
import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.practicum.playlistmaker.domain.search.repository.SearchRepository
import com.practicum.playlistmaker.domain.settings.api.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.api.SharingInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.settings.impl.SharingInteractorImpl
import com.practicum.playlistmaker.domain.settings.repository.SettingsRepository
import com.practicum.playlistmaker.domain.settings.repository.SharingRepository
import com.practicum.playlistmaker.domain.track.api.TrackInteractor
import com.practicum.playlistmaker.domain.track.impl.TrackInteractorImpl
import com.practicum.playlistmaker.domain.track.repository.TrackRepository

object Creator {

    fun provideTrackInteractor(context: Context): TrackInteractor = TrackInteractorImpl(getTrackRepository(context))
    private fun getTrackRepository(context: Context): TrackRepository = TrackRepositoryImpl(getNetworkClient(context))
    private fun getNetworkClient(context: Context): NetworkClient = RetrofitNetworkClient(context)

    fun provideSearchInteractor(): SearchInteractor = SearchInteractorImpl(getSearchRepository())
    private fun getSearchRepository(): SearchRepository = SearchRepositoryImpl(getSearchLocalStorage())
    private fun getSearchLocalStorage(): SearchLocalStorage = SearchLocalStorageImpl(getSharedPreferences())
    private fun getSharedPreferences() = App.sharedPreferences

    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorImpl(getPlayerRepository())
    private fun getPlayerRepository(): PlayerRepository = PlayerRepositoryImpl(getPlayer())
    private fun getPlayer(): Player = PlayerImpl(MediaPlayer())

    fun provideSettingsInteractor(application: Application): SettingsInteractor = SettingsInteractorImpl(getSettingsRepository(application))
    private fun getSettingsRepository(application: Application): SettingsRepository = SettingsRepositoryImpl(getSettingsLocalStorage(application))
    private fun getSettingsLocalStorage(application: Application): SettingsLocalStorage = SettingsLocalStorageImpl(application)

    fun provideSharingInteractor(context: Context): SharingInteractor = SharingInteractorImpl(getSharedRepository(context))
    private fun getSharedRepository(context: Context): SharingRepository = SharingRepositoryImpl(getExternalNavigator(context))
    private fun getExternalNavigator(context: Context): ExternalNavigator = ExternalNavigatorImpl(context)



}