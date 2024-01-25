package com.practicum.playlistmaker.di

import android.app.Application
import android.content.SharedPreferences
import android.media.MediaPlayer
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
import com.practicum.playlistmaker.domain.player.repository.PlayerRepository
import com.practicum.playlistmaker.domain.search.repository.SearchRepository
import com.practicum.playlistmaker.domain.settings.repository.SettingsRepository
import com.practicum.playlistmaker.domain.settings.repository.SharingRepository
import com.practicum.playlistmaker.domain.track.repository.TrackRepository
import org.koin.dsl.module

val dataModule = module {
    single<TrackRepository> { TrackRepositoryImpl(networkClient = get()) }
    single<SearchRepository> { SearchRepositoryImpl(storage = get()) }
    single<PlayerRepository> { PlayerRepositoryImpl(player = get()) }
    single<SharingRepository> { SharingRepositoryImpl(externalNavigator = get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(storage = get()) }

    single<SearchLocalStorage> { (pref: SharedPreferences) -> SearchLocalStorageImpl(sharedPreferences = pref) }
    single<SettingsLocalStorage> { (application: Application) -> SettingsLocalStorageImpl(application = application) }

    single<ExternalNavigator> { ExternalNavigatorImpl(context = get()) }
    single<Player> { (player: MediaPlayer) -> PlayerImpl(player = player) }
}