package com.practicum.playlistmaker.presentation.search.view_model

import com.practicum.playlistmaker.domain.track.Track

sealed interface SearchState {
    object Loading: SearchState
    data class Content (val trackList: List<Track>): SearchState
    data class Error (val errorMessage: String): SearchState
}