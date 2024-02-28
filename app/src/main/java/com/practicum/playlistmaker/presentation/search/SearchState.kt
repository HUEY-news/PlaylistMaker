package com.practicum.playlistmaker.presentation.search

import com.practicum.playlistmaker.domain.track.model.Track

sealed interface SearchState {
    object Loading: SearchState
    data class Content (val trackList: List<Track>): SearchState
    data class Error (val errorMessage: String): SearchState
}