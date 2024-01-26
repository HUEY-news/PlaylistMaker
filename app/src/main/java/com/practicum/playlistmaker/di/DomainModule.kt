package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.player.repository.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.settings.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.settings.repository.SharingRepositoryImpl
import com.practicum.playlistmaker.data.track.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.player.repository.PlayerRepository
import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.search.repository.SearchHistoryRepository
import com.practicum.playlistmaker.domain.settings.api.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.api.SharingInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.settings.impl.SharingInteractorImpl
import com.practicum.playlistmaker.domain.settings.repository.SettingsRepository
import com.practicum.playlistmaker.domain.settings.repository.SharingRepository
import com.practicum.playlistmaker.domain.track.api.TrackInteractor
import com.practicum.playlistmaker.domain.track.impl.TrackInteractorImpl
import com.practicum.playlistmaker.domain.track.repository.TrackRepository
import org.koin.dsl.module

val interactorModule = module {

    factory<TrackInteractor> { TrackInteractorImpl(repository = get()) }
    factory<SearchInteractor> { SearchHistoryInteractorImpl(repository = get()) }
    factory<PlayerInteractor> { PlayerInteractorImpl(repository = get()) }
    factory<SharingInteractor> { SharingInteractorImpl(repository = get()) }
    factory<SettingsInteractor> { SettingsInteractorImpl(repository = get()) }
}

val repositoryModule = module {

    single<TrackRepository> { TrackRepositoryImpl(networkClient = get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(storage = get()) }
    single<PlayerRepository> { PlayerRepositoryImpl(player = get()) }
    single<SharingRepository> { SharingRepositoryImpl(externalNavigator = get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(storage = get()) }
}



