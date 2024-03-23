package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.presentation.library.LibraryFavouriteViewModel
import com.practicum.playlistmaker.presentation.library.LibraryPlaylistViewModel
import com.practicum.playlistmaker.presentation.player.PlayerViewModel
import com.practicum.playlistmaker.presentation.search.SearchViewModel
import com.practicum.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PlayerViewModel(interactor = get()) }
    viewModel { SearchViewModel(trackInteractor = get(), searchHistoryInteractor = get()) }
    viewModel { SettingsViewModel(settingsInteractor = get(), sharingInterractor = get()) }

    viewModel { LibraryFavouriteViewModel() }
    viewModel { LibraryPlaylistViewModel() }
}