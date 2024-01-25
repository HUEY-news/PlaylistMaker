package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.practicum.playlistmaker.domain.settings.api.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.api.SharingInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.settings.impl.SharingInteractorImpl
import com.practicum.playlistmaker.domain.track.api.TrackInteractor
import com.practicum.playlistmaker.domain.track.impl.TrackInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    factory<TrackInteractor> { TrackInteractorImpl(repository = get()) }
    factory<SearchInteractor> { SearchInteractorImpl(repository = get()) }
    factory<PlayerInteractor> { PlayerInteractorImpl(repository = get()) }
    factory<SharingInteractor> { SharingInteractorImpl(repository = get()) }
    factory<SettingsInteractor> { SettingsInteractorImpl(repository = get()) }
}