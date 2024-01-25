package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.presentation.search.view_model.SearchViewModel
import com.practicum.playlistmaker.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<PlayerViewModel> { PlayerViewModel(interactor = get()) }
    viewModel<SearchViewModel> { SearchViewModel(trackInteractor = get(), searchInteractor = get()) }
    viewModel<SettingsViewModel> { SettingsViewModel(settingsInteractor = get(), sharingInterractor = get()) }
}