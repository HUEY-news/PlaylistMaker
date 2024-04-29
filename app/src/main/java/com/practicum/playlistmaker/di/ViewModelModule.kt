package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.presentation.library.LibraryFavoriteViewModel
import com.practicum.playlistmaker.presentation.library.LibraryNewPlaylistViewModel
import com.practicum.playlistmaker.presentation.library.LibraryPlaylistViewModel
import com.practicum.playlistmaker.presentation.library.PlaylistViewModel
import com.practicum.playlistmaker.presentation.player.PlayerViewModel
import com.practicum.playlistmaker.presentation.search.SearchViewModel
import com.practicum.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PlayerViewModel(playerInteractor = get(), favoriteInteractor = get(), playlistInteractor = get()) }
    viewModel { SearchViewModel(trackInteractor = get(), searchHistoryInteractor = get()) }
    viewModel { SettingsViewModel(settingsInteractor = get(), sharingInterractor = get()) }

    viewModel { LibraryFavoriteViewModel(favoriteInteractor = get()) }
    viewModel { LibraryPlaylistViewModel(playlistInteractor = get()) }
    viewModel { LibraryNewPlaylistViewModel(playlistInteractor = get()) }
    viewModel { PlaylistViewModel(playlistInteractor = get()) }
}