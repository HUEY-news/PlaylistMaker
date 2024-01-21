package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.practicum.playlistmaker.creator.Creator

class SearchViewModelFactory: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val application = checkNotNull(extras.get(APPLICATION_KEY))
        return SearchViewModel(Creator.provideTrackInteractor(application)) as T
    }
}