package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.convertor.DbConvertor
import com.practicum.playlistmaker.data.library.FavoriteRepositoryImpl
import com.practicum.playlistmaker.data.library.PlaylistRepositoryImpl
import com.practicum.playlistmaker.data.player.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.search.TrackRepositoryImpl
import com.practicum.playlistmaker.data.settings.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.settings.SharingRepositoryImpl
import com.practicum.playlistmaker.domain.library.FavoriteInteractor
import com.practicum.playlistmaker.domain.library.FavoriteInteractorImpl
import com.practicum.playlistmaker.domain.library.FavoriteRepository
import com.practicum.playlistmaker.domain.library.PlaylistInteractor
import com.practicum.playlistmaker.domain.library.PlaylistInteractorImpl
import com.practicum.playlistmaker.domain.library.PlaylistRepository
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.search.SearchHistoryRepository
import com.practicum.playlistmaker.domain.search.TrackInteractor
import com.practicum.playlistmaker.domain.search.TrackInteractorImpl
import com.practicum.playlistmaker.domain.search.TrackRepository
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.SharingInteractor
import com.practicum.playlistmaker.domain.settings.SharingInteractorImpl
import com.practicum.playlistmaker.domain.settings.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val interactorModule = module {

    factory<TrackInteractor> { TrackInteractorImpl(repository = get()) }
    factory<SearchHistoryInteractor> { SearchHistoryInteractorImpl(repository = get()) }
    factory<PlayerInteractor> { PlayerInteractorImpl(repository = get()) }
    factory<SharingInteractor> { SharingInteractorImpl(repository = get()) }
    factory<SettingsInteractor> { SettingsInteractorImpl(repository = get()) }
    factory<FavoriteInteractor> { FavoriteInteractorImpl(repository = get()) }
    factory<PlaylistInteractor> { PlaylistInteractorImpl(repository = get()) }
}

val repositoryModule = module {

    single<TrackRepository> { TrackRepositoryImpl(context = androidContext(), client = get(), appDatabase = get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(storage = get()) }
    single<PlayerRepository> { PlayerRepositoryImpl(player = get()) }
    single<SharingRepository> { SharingRepositoryImpl(externalNavigator = get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(storage = get()) }

    factory { DbConvertor() }
    single<FavoriteRepository> { FavoriteRepositoryImpl(appDatabase = get(), dbConvertor = get()) }
    single<PlaylistRepository> { PlaylistRepositoryImpl(appDatabase = get(), dbConvertor = get(), gson = get()) }
}